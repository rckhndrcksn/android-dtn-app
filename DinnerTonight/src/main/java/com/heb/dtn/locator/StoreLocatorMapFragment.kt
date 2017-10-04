//
// Created by Khuong Huynh on 11/28/16.
//

package com.heb.dtn.locator

import android.content.Context
import android.graphics.Bitmap
import android.location.Location
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.heb.dtn.R
import com.heb.dtn.utils.AccessPermission
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.heb.dtn.app.AppProxy
import com.heb.dtn.locator.domain.StoreItem
import com.heb.dtn.extensions.metersToMiles
import com.heb.dtn.extensions.roundTo

class StoreLocatorMapFragment : Fragment(), StoreLocatorStoreAdapter {

    interface MapViewListener {
        fun onMapLoaded()
        fun onCameraLocationChange(latNLng: LatLng, radius: Double = 0.0)
        fun onMarkerClick(storeItem: StoreItem?)
        fun onMarkerDeselected()
    }

    lateinit private var googleMap: GoogleMap
    private var mapView: MapView? = null
    private var clusterManager: ClusterManager<StoreItem>? = null
    private var lastClickedMarker: StoreItem? = null

    var mapViewListener: MapViewListener? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_store_locator_map, container, false)

        this.mapView = rootView?.findViewById(R.id.mapView) as MapView

        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        this.mapView?.onCreate(savedInstanceState)
        this.mapView?.getMapAsync { map ->
            this.googleMap = map

            this.clusterManager = ClusterManager<StoreItem>(this.context, this.googleMap)
            this.clusterManager?.renderer = CustomIconRender(this.context, this.googleMap, this.clusterManager)
            this.clusterManager?.setOnClusterClickListener { this.onClusterClicked(it) }
            this.clusterManager?.setOnClusterItemClickListener { this.onMarkerClicked(it) }

            this.googleMap.uiSettings.isRotateGesturesEnabled = false
            this.googleMap.setOnCameraIdleListener { this.onCameraIdle() }
            this.googleMap.setOnMarkerClickListener(this.clusterManager)
            this.googleMap.setOnMapClickListener { this.onMapClicked(it) }

            // For showing a center to my location button on map
            if (AccessPermission.isLocationAccessGranted) {
                this.onLocationEnable()
            }

            this.mapViewListener?.onMapLoaded()
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

    override fun setStoreItems(stores: List<StoreItem>?) {
        if (stores?.contains(this.lastClickedMarker) == false) {
            this.lastClickedMarker = null
            this.activity.runOnUiThread { this.mapViewListener?.onMarkerDeselected() }
        }

        this.clusterManager?.clearItems()
        this.clusterManager?.addItems(stores)

        this.activity.runOnUiThread {
            this.clusterManager?.cluster()
        }
    }

    fun onLocationEnable() {
        this.googleMap.uiSettings.isMyLocationButtonEnabled = false
        this.googleMap.isMyLocationEnabled = true
    }

    fun requestMapSnapshot(onSnapshotCaptured: ((bitmap: Bitmap) -> Unit)) {
        this.googleMap.snapshot { bitmap ->  onSnapshotCaptured.invoke(bitmap) }
    }

    fun zoomAndSelectStore(storeItem: StoreItem, onZoomEnd: (() -> Unit)? = null) {
        this.zoomToLatLng(latLng = storeItem.position, animate = false)
        this.onMarkerClicked(storeItem)

        this.googleMap.setOnMapLoadedCallback { onZoomEnd?.invoke() }
    }

    fun zoomToLatLng(latLng: LatLng, zoomLevel: Int = AppProxy.proxy.resources.getInteger(R.integer.default_store_details_zoom), animate: Boolean = true) {
        val center = CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel.toFloat())

        when(animate) {
            true -> this.googleMap.animateCamera(center)
            else -> this.googleMap.moveCamera(center)
        }
    }

    fun refresh() { this.onCameraIdle() }

    //
    // Private methods
    //
    private fun setSmallIconForLastClickedMarker() {
        val renderer = this.clusterManager?.renderer as CustomIconRender?
        renderer?.getMarker(this.lastClickedMarker)?.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marker_small))
    }

    private fun onMapClicked(latLng: LatLng) {
        if (this.lastClickedMarker?.position?.equals(latLng) == false) {
            this.setSmallIconForLastClickedMarker()

            this.lastClickedMarker = null
            this.mapViewListener?.onMarkerDeselected()
        }
    }

    private fun onClusterClicked(cluster: Cluster<StoreItem>): Boolean {
        val zoomLvl = (this.googleMap.cameraPosition.zoom + 1.0).toInt()

        this.zoomToLatLng(latLng = cluster.position, zoomLevel = zoomLvl, animate = true)

        return true
    }

    private fun onMarkerClicked(storeItem: StoreItem): Boolean {
        if (this.lastClickedMarker?.equals(storeItem) == false) {
            this.setSmallIconForLastClickedMarker()
        }

        val renderer = this.clusterManager?.renderer as CustomIconRender?
        renderer?.getMarker(storeItem)?.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marker_large))
        this.lastClickedMarker = storeItem

        this.mapViewListener?.onMarkerClick(storeItem)

        return false
    }

    private fun onCameraIdle() {
//        this.googleMap.uiSettings.setAllGesturesEnabled(false)
        val latLngBounds = this.googleMap.projection.visibleRegion.latLngBounds

        val centerLatLng = latLngBounds.center
        val radius = latLngBounds.getRadiusInMiles()

        this.mapViewListener?.onCameraLocationChange(centerLatLng, Math.round(radius).toDouble())
        this.clusterManager?.onCameraIdle()
    }

    inner class CustomIconRender(context: Context?, map: GoogleMap?, clusterManager: ClusterManager<StoreItem>?)
        : DefaultClusterRenderer<StoreItem>(context, map, clusterManager) {

        val icon: Bitmap? = null
        val color: Int = ContextCompat.getColor(context, R.color.mapCluster)

        override fun getColor(clusterSize: Int): Int = this.color

        override fun onBeforeClusterItemRendered(item: StoreItem?, markerOptions: MarkerOptions?) {
            var resourceId = R.drawable.icon_marker_small
            if (this@StoreLocatorMapFragment.lastClickedMarker?.equals(item) == true) {
                resourceId = R.drawable.icon_marker_large
            }

            markerOptions?.icon(BitmapDescriptorFactory.fromResource(resourceId))
        }
    }

    //
    // Extensions
    //

    private fun LatLngBounds.getRadiusInMiles(): Double {
        val northEast = this.northeast
        val southWest = this.southwest

        val res = FloatArray(2)

        Location.distanceBetween(northEast.latitude, northEast.longitude, southWest.latitude, southWest.longitude, res)
        return (res[0] / 2).metersToMiles().roundTo(2)
    }
}
