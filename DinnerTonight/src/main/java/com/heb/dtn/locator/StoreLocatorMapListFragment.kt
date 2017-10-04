package com.heb.dtn.locator

import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.BottomSheetBehavior
import android.widget.ProgressBar
import com.google.android.gms.maps.model.LatLng
import com.heb.dtn.R
import com.heb.dtn.app.AppProxy
import com.heb.dtn.locator.domain.StoreItem
import com.heb.dtn.service.domain.store.Store
import kotlinx.android.synthetic.main.fragment_store_locator_map_list.*
import kotlinx.android.synthetic.main.view_store_info.view.*
import android.support.design.widget.CoordinatorLayout
import android.support.v4.content.ContextCompat
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.SearchView
import android.util.TypedValue
import android.view.*
import android.widget.TextView
import com.heb.dtn.foundation.promise.android.always
import com.heb.dtn.foundation.promise.android.catch
import com.heb.dtn.foundation.promise.android.then
import com.heb.dtn.extensions.*
//import com.heb.dtn.service.pharmacy.api.PharmacyServiceError
//import com.heb.dtn.service.pharmacy.api.PharmacyServiceErrorCode
//import com.heb.dtn.service.pharmacy.domain.store.StoreFeatureCode

data class StoreLocatorSelectedStore(val userLocation: Location, val store: StoreItem)

class StoreLocatorMapListFragment : LocatorFragment<StoreLocatorOption, StoreLocatorSelectedStore>() {

    private var spinner: ProgressBar? = null
    private var listFragment: StoreLocatorListFragment? = null
    private var mapFragment: StoreLocatorMapFragment? = null
    private var searching = false
    lateinit private var lastUserLocation: Location

    private val DEFAULT_RADIUS = AppProxy.proxy.resources.getDoubleFromString(R.string.default_radius)

    override fun createView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        val rootView = inflater?.inflate(R.layout.fragment_store_locator_map_list, container, false)

        rootView?.setBackgroundColor(ContextCompat.getColor(this.activity, R.color.defaultBackground))

        return rootView
    }

    override fun flowWillRun(args: StoreLocatorOption) {
        if (args == StoreLocatorOption.SELECT_STORE) {
            this.titleResId = R.string.view_title_select_pharmacy
        } else {
            this.titleResId = R.string.view_title_pharmacy_locator
        }
        super.flowWillRun(args)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.initializeLocator()

        // Setup
        val store = AppProxy.proxy.defaultCenterMapStore

        this.lastUserLocation = Location("")
        this.lastUserLocation.latitude = store.latitude ?: 0.0
        this.lastUserLocation.longitude = store.longitude ?: 0.0

        this.spinner = this.showActivityIndicator()

        this.locationButton.setOnClickListener { this.updatePharmacyStoresWithCurrentLocation() }

        this.mapFragment = this.childFragmentManager.findFragmentById(R.id.mapFragment) as StoreLocatorMapFragment?
        this.mapFragment?.mapViewListener = this.mapViewListener

        this.listFragment = this.childFragmentManager.findFragmentById(R.id.listFragment) as StoreLocatorListFragment?
        this.listFragment?.itemListener = this.listItemListener
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        if (menu?.findItem(R.id.search) == null) {
            menu?.clear()
            inflater?.inflate(R.menu.locator_menu, menu)
        }

        val menuItem = menu?.findItem(R.id.search)
        val searchView = MenuItemCompat.getActionView(menuItem) as SearchView

        val v = searchView.findViewById(android.support.v7.appcompat.R.id.search_plate)
        v.setBackgroundColor(searchView.getCompatColor(R.color.defaultBackground))

        searchView.queryHint = this.getString(R.string.rx_locator_search_placeholder)

        val searchTextView = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text) as TextView
        searchTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, this.resources.getDimension(R.dimen.text_large))

        MenuItemCompat.setOnActionExpandListener(menuItem, object : MenuItemCompat.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                menu?.findItem(R.id.cancel)?.isVisible = false
                this@StoreLocatorMapListFragment.showSearch()

                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                menu?.findItem(R.id.cancel)?.isVisible = true
                this@StoreLocatorMapListFragment.showNearby()
                return true
            }
        })

        searchView.setOnQueryTextListener( object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { this@StoreLocatorMapListFragment.searchPharmacyStores(query) }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean = false
        })
    }

    override fun onLocationRetrieved(location: Location, isLocationEnabled: Boolean) {
        this.lastUserLocation = location
        this.zoomToUserLocation(location)
        if (isLocationEnabled) this.mapFragment?.onLocationEnable()
        this.activity.runOnUiThread { this.spinner?.hide() }
    }

    override fun onPermissionDenied() {
        this.view?.snack(R.string.location_services_disabled) {
            this.action(R.string.location_services_app_settings_button) {
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts("package", this@StoreLocatorMapListFragment.activity.packageName, null)
                intent.data = uri
                this@StoreLocatorMapListFragment.activity.startActivity(intent)
            }
        }
        this.activity.runOnUiThread { this.spinner?.hide() }
    }

    //
    // Private Methods
    //
    private fun showSearch() {
        this.searching = true
        this.listContainer.expand()

        this.hidePharmacyInfoAndShowList()
        this.listFragment?.switchToSearchList()
    }

    private fun showNearby() {
        this.searching = false
        this.listContainer.collapse()
        this.listFragment?.switchToNearByList()

        // Refresh map camera to get the nearby locations
        this.mapFragment?.refresh()
    }

    private fun populatePharmacyInfoView(storeItem: StoreItem) {
        this.selectedPharmacyInfo.title.text = storeItem.name
        this.selectedPharmacyInfo.address1.text = storeItem.address1
        this.selectedPharmacyInfo.address2.text = storeItem.address2
        this.selectedPharmacyInfo.distance.text = "${storeItem.distanceToLocation} mi."
        this.selectedPharmacyInfo.setOnClickListener { this.listFragment?.itemListener?.onItemClicked(storeItem) }
    }

    private fun hideListAndShowPharmacyInfo() {
        this.listContainer.setPeekHeight(0)
        this.fabButtonContainer.switchAnchorTo(R.id.selectedPharmacyInfo)
        this.googleLogoContainer.switchAnchorTo(R.id.selectedPharmacyInfo)
        this.selectedPharmacyInfo.show()
    }

    private fun hidePharmacyInfoAndShowList() {
        this.resetListContainerPosition()
        this.fabButtonContainer.switchAnchorTo(R.id.listContainer)
        this.googleLogoContainer.switchAnchorTo(R.id.listContainer)
        this.selectedPharmacyInfo.hide()
    }

    private fun resetListContainerPosition() {
        val peekHeight = this.resources.getDimensionPixelSize(R.dimen.fragment_map_list_peek_height)
        this.listContainer.setPeekHeight(peekHeight)
    }

    private fun setStores(stores: List<Store>) {
        if (stores.isEmpty()) return
        val sortedItems = stores.map {
                    val storeItem = StoreItem(it)
                    storeItem.setDistanceToLocation(this.lastUserLocation)
                    storeItem
                }.sortedBy { it.distanceToLocation }

        this.activity.runOnUiThread {
            this.listFragment?.setData(sortedItems)
        }

        this.mapFragment?.setStoreItems(sortedItems)
    }

    private fun updatePharmacyStoresWithCurrentLocation() {
        this.activity.runOnUiThread { this.spinner?.show() }

        this.locationServiceManager?.requestOneShotLocationUpdate()
    }

    private fun zoomToUserLocation(location: Location) {
        this.activity.runOnUiThread { this.mapFragment?.zoomToLatLng(LatLng(location.latitude, location.longitude), 13) }
        this.lastUserLocation = location
    }

    private fun updatePharmacyStores(location: Location, radius: Double = this.DEFAULT_RADIUS) {
        this.spinner?.show()

        val service = AppProxy.proxy.serviceManager().storeService()

        service.getStores(location.latitude, location.longitude, radius)
                .then { result -> this.setStores(result.items) }
                .always { this.spinner?.hide() }
    }

    private fun searchPharmacyStores(zipcode: String, radius: Double? = null) {
        /*
        this.spinner?.show()

        val service = AppProxy.proxy.serviceManager().storeService()

        service.getStores(zipcode, radius, listOf(StoreFeatureCode.PHARMACY))
                .then { result ->
                    this.setStores(result.stores) }
                .catch {
                    val error = it as PharmacyServiceError
                    val message = when (error.code) {
                        PharmacyServiceErrorCode.INVALID_STORE_SEARCH_ZIPCODE -> this.getString(R.string.invalid_store_search_zipcode)
                        PharmacyServiceErrorCode.INVALID_STORE_SEARCH_CITY_STATE -> this.getString(R.string.invalid_store_search_city_state)
                        else -> null
                    }

                    if (message.isNullOrEmpty()) {
                        this.showErrorDialog(it)
                    } else {
                        this.showDialog(title = this.getString(R.string.error), message = message)
                    }
                }
                .always { this.spinner?.hide() }
                */
    }

    //
    // Listeners
    //

    private val mapViewListener: StoreLocatorMapFragment.MapViewListener
        get() = object : StoreLocatorMapFragment.MapViewListener {
            override fun onMapLoaded() { this@StoreLocatorMapListFragment.updatePharmacyStoresWithCurrentLocation() }

            override fun onCameraLocationChange(latNLng: LatLng, radius: Double) {
                if (this@StoreLocatorMapListFragment.searching) return

                val location = Location("")
                location.latitude = latNLng.latitude
                location.longitude = latNLng.longitude

                this@StoreLocatorMapListFragment.updatePharmacyStores(location, radius)
            }

            override fun onMarkerClick(storeItem: StoreItem?) {
                storeItem?.let {
                    this@StoreLocatorMapListFragment.populatePharmacyInfoView(storeItem)
                    this@StoreLocatorMapListFragment.hideListAndShowPharmacyInfo()
                }
            }
            override fun onMarkerDeselected() { this@StoreLocatorMapListFragment.hidePharmacyInfoAndShowList() }
        }

    private val listItemListener: StoreLocatorListFragment.ListItemListener
        get() = object : StoreLocatorListFragment.ListItemListener {
            override fun onItemClicked(storeItem: StoreItem) {
                val location = this@StoreLocatorMapListFragment.lastUserLocation
                this@StoreLocatorMapListFragment.finish(StoreLocatorSelectedStore(userLocation = location, store = storeItem))
            }
        }

    //
    // Extensions
    //
    private fun ViewGroup.switchAnchorTo(id: Int) {
        val layoutParams = this.layoutParams as CoordinatorLayout.LayoutParams
        layoutParams.anchorId = id
        this.layoutParams = layoutParams
    }

    private fun ViewGroup.setPeekHeight(height: Int) {
        val behavior = BottomSheetBehavior.from(this)
        behavior.peekHeight = height
    }

    private fun ViewGroup.expand() {
        val behavior = BottomSheetBehavior.from(this)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun ViewGroup.collapse() {
        val behavior = BottomSheetBehavior.from(this)
        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }
}
