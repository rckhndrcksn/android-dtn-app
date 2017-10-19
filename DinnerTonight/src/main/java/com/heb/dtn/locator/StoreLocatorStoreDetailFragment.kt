package com.heb.dtn.locator

import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.heb.dtn.R
import com.heb.dtn.app.AppProxy
import com.heb.dtn.locator.domain.StoreItem
import com.heb.dtn.extensions.hide
import com.heb.dtn.extensions.show
import kotlinx.android.synthetic.main.fragment_store_locator_store_details.*

enum class StoreLocatorOption {
    DEFAULT,
    SELECT_STORE,
    CHANGE_STORE,
    SAVE_PREFERRED_STORE
}

data class StoreDetailParams(
        val storeItem: StoreItem,
        var title: String? = null,
        var option: StoreLocatorOption = StoreLocatorOption.DEFAULT,
        var userLocation: Location? = null
)

class StoreLocatorStoreDetailFragment : StoreLocatorFragment<StoreDetailParams, StoreItem>() {

    override fun flowWillRun(args: StoreDetailParams) {
        this.activity.title = args.title
        if (args.title.isNullOrEmpty()) {
            this.titleResId = if (args.option == StoreLocatorOption.SELECT_STORE) {
                R.string.view_title_select_pharmacy
            } else {
                R.string.view_title_pharmacy_locator
            }
        }
        this.updateView(args)
        super.flowWillRun(args)
    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        val rootView = inflater.inflate(R.layout.fragment_store_locator_store_details, container, false)
        rootView?.setBackgroundColor(Color.WHITE)
        (rootView?.findViewById(R.id.mapView) as MapView).onCreate(savedInstanceState)

        return rootView
    }

    private fun updateView(pharmacyDetailParams: StoreDetailParams) {
        this.mapView.isClickable = false
        this.populateTitle(pharmacyDetailParams)

        this.mapView.hide()
        this.mapSpinner.show()

        val option = pharmacyDetailParams.option

        if (option == StoreLocatorOption.SELECT_STORE) {
            this.titleResId = R.string.view_title_select_pharmacy
        } else if (option == StoreLocatorOption.SAVE_PREFERRED_STORE) {
            this.titleResId = R.string.view_title_pharmacy_locator
        }

        val storeItem = pharmacyDetailParams.storeItem
        if (pharmacyDetailParams.userLocation == null) {
            this.distance.hide(true)
        } else {
            pharmacyDetailParams.userLocation?.let { storeItem.setDistanceToLocation(it) }
            this.distance.show()
        }

        this.mapView.getMapAsync { map ->
            val location = storeItem.position
            val zoomLevel = AppProxy.proxy.resources.getInteger(R.integer.default_store_details_zoom)
            val storePosition = LatLng(location.latitude, location.longitude)
            val center = CameraUpdateFactory.newLatLngZoom(storePosition, zoomLevel.toFloat())

            map.addMarker(MarkerOptions()
                    .position(storePosition)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marker_large)))
            map.moveCamera(center)
            map.setOnMapLoadedCallback {
                this.mapView.show()
                this.mapSpinner.hide()
            }
        }

        // Setup
        this.title.text = storeItem.name
        this.address1.text = storeItem.address1
        this.address2.text = storeItem.address2
        this.distance.text = "${storeItem.distanceToLocation} mi."

        /*
        if (storeItem.pharmacyHours.isNotEmpty()) {
            this.hours.show()
            this.hoursSubtitle.show()
            this.hours.text = storeItem.pharmacyHours
        }
        */

        // Location data check
        if (this.validCoordinate(storeItem.position.latitude)
                && this.validCoordinate(storeItem.position.longitude)) {
            this.navigateButton.show()
            this.navigateButton.setOnClickListener {
                val position = storeItem.position
                val uri = Uri.parse("https://maps.google.com/maps?daddr=${position.latitude},${position.longitude}")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                intent.`package` = "com.google.android.apps.maps"

                if (intent.resolveActivity(this.activity.packageManager) != null) {
                    this.startActivity(intent);
                }
            }
        }

        // Phone number check
        if (storeItem.storePhone.isNotEmpty()) {
            this.phoneContainer.show()
            this.phoneNumber.text = storeItem.storePhone
            this.phoneContainer.setOnClickListener {
                val callIntent = Intent(Intent.ACTION_DIAL)
                callIntent.data = Uri.parse("tel:${storeItem.storePhone}")
                this.startActivity(callIntent)
            }
        }

        val buttonText = when(pharmacyDetailParams.option) {
            StoreLocatorOption.SELECT_STORE ->
                getString(R.string.view_title_select_pharmacy)
            StoreLocatorOption.CHANGE_STORE ->
                getString(R.string.rx_locator_change_store)
            StoreLocatorOption.SAVE_PREFERRED_STORE ->
                getString(R.string.rx_locator_save_preferred_pharmacy)
            StoreLocatorOption.DEFAULT -> ""
        }

        if (buttonText.isNotEmpty()) {
            this.actionButton.text = buttonText
            this.actionButton.show()
            this.actionButton.setOnClickListener({ this.finish(pharmacyDetailParams.storeItem)})
        }
    }

    //
    // Private
    //
    private fun validCoordinate(coordinate: Double): Boolean
        = !coordinate.isNaN() && coordinate != 0.0

    private fun populateTitle(pharmacyDetailParams: StoreDetailParams) {
        val title = pharmacyDetailParams.title ?: when(pharmacyDetailParams.option) {
            StoreLocatorOption.SELECT_STORE ->
                getString(R.string.view_title_select_pharmacy)
            StoreLocatorOption.CHANGE_STORE, StoreLocatorOption.SAVE_PREFERRED_STORE ->
                getString(R.string.view_title_pharmacy_locator)
            StoreLocatorOption.DEFAULT ->
                    pharmacyDetailParams.title ?: ""
        }
        this.activity.title = title
    }
}
