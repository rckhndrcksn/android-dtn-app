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
import android.view.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.heb.dtn.foundation.promise.android.always
import com.heb.dtn.foundation.promise.android.then
import com.heb.dtn.extensions.*
import com.heb.dtn.utils.AccessPermission
import com.heb.dtn.widget.DTNMapView

data class StoreLocatorSelectedStore(val userLocation: Location, val store: StoreItem)

class StoreLocatorMapListFragment : LocatorFragment<StoreLocatorOption, StoreLocatorSelectedStore>() {

    private var spinner: ProgressBar? = null
    private var listFragment: StoreLocatorListFragment? = null
    private var searching = false
    lateinit private var lastUserLocation: Location
    lateinit private var googleMap: GoogleMap

    private val DEFAULT_RADIUS = AppProxy.proxy.resources.getDoubleFromString(R.string.default_radius)

    override fun createView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater?.inflate(R.layout.fragment_store_locator_map_list, container, false)
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

        val bottomSheetLayout = BottomSheetBehavior.from(listContainer);
        val peekHeight = bottomSheetLayout.peekHeight
        bottomSheetLayout.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {}

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheetIndicator.setImageResource(R.drawable.ic_collapsed)
                } else if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetIndicator.setImageResource(R.drawable.svg_ic_expanded)
                }
            }

        })
        coordinatorLayout?.let {
            coordinatorLayout.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    val height = coordinatorLayout.measuredHeight
                    val availableSpace = height - peekHeight
                    if (availableSpace > 0) {
                        val params = mapView.layoutParams
                        params.height = availableSpace
                        mapView.layoutParams = params
                    }
                    if (height > 0) {
                        coordinatorLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        this@StoreLocatorMapListFragment.loadMap()
                    }
                }
            })
        }


        this.listFragment = this.childFragmentManager.findFragmentById(R.id.listFragment) as StoreLocatorListFragment?
        if (this.listFragment == null) {
            val ft = this.childFragmentManager.beginTransaction()
            this.listFragment = StoreLocatorListFragment()
            ft.replace(R.id.listFragment, this.listFragment)
            ft.commit()
        }
        this.listFragment?.itemListener = this.listItemListener

        this.mapView?.setMapViewListener(this.mapViewListener)
        this.mapView?.onCreate(savedInstanceState)
    }

    private fun loadMap() {
        this.mapView?.getMapAsync { map ->
            this.googleMap = map
            this.googleMap.uiSettings.isRotateGesturesEnabled = false

            // For showing a center to my location button on map
            if (AccessPermission.isLocationAccessGranted) {
                this.onLocationEnable()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        this.mapView?.onStop()
    }

    override fun onResume() {
        super.onResume()
        this.mapView?.onResume()
    }

    override fun onPause() {
        this.mapView?.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        this.mapView?.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        this.mapView?.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        this.mapView?.onSaveInstanceState(outState)
    }

    override fun onLocationRetrieved(location: Location, isLocationEnabled: Boolean) {
        this.lastUserLocation = location
        this.zoomToUserLocation(location)
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
    private fun onLocationEnable() {
        this.googleMap.uiSettings.isMyLocationButtonEnabled = false
        this.googleMap.isMyLocationEnabled = true
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

        this.mapView?.setStoreItems(sortedItems)
    }

    private fun updatePharmacyStoresWithCurrentLocation() {
        this.activity.runOnUiThread { this.spinner?.show() }

        this.locationServiceManager?.requestOneShotLocationUpdate()
    }

    private fun zoomToUserLocation(location: Location) {
        zoomToLatLng(LatLng(location.latitude, location.longitude), 13)
        this.lastUserLocation = location
    }

    private fun updatePharmacyStores(location: Location, radius: Double = this.DEFAULT_RADIUS) {
        this.spinner?.show()

        val service = AppProxy.proxy.serviceManager().storeService()

        service.getStores(location.latitude, location.longitude, radius)
                .then { result -> this.setStores(result.items) }
                .always { this.spinner?.hide() }
    }

    //
    // Private methods
    //
    private fun zoomToLatLng(latLng: LatLng, zoomLevel: Int = AppProxy.proxy.resources.getInteger(R.integer.default_store_details_zoom), animate: Boolean = true) {
        val center = CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel.toFloat())

        when(animate) {
            true -> this.googleMap.animateCamera(center)
            else -> this.googleMap.moveCamera(center)
        }
    }

    //
    // Listeners
    //

    private val mapViewListener: DTNMapView.MapViewListener
        get() = object : DTNMapView.MapViewListener {
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
                    val location = this@StoreLocatorMapListFragment.lastUserLocation
                    this@StoreLocatorMapListFragment.finish(StoreLocatorSelectedStore(userLocation = location, store = storeItem))
                }
            }
            override fun onMarkerDeselected() {  }
        }

    private val listItemListener: StoreLocatorListFragment.ListItemListener
        get() = object : StoreLocatorListFragment.ListItemListener {
            override fun onItemClicked(storeItem: StoreItem) {
                val location = this@StoreLocatorMapListFragment.lastUserLocation
                this@StoreLocatorMapListFragment.finish(StoreLocatorSelectedStore(userLocation = location, store = storeItem))
            }
        }
}
