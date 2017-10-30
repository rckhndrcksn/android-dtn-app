package com.heb.dtn.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.heb.dtn.R
import com.heb.dtn.extensions.metersToMiles
import com.heb.dtn.extensions.roundTo
import com.heb.dtn.locator.domain.StoreItem

/**
 * Created by jcarbo on 10/27/17.
 */
class DTNMapView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): MapView(context, attrs, defStyleAttr) {

    private var lastClickedMarker: StoreItem? = null
    private var clusterManager: ClusterManager<StoreItem>? = null
    private var defaultZoom: Int = context.resources.getInteger(R.integer.default_store_details_zoom)
    lateinit private var googleMap: GoogleMap

    private var largeClusterIcon: Bitmap? = null
    private var smallClusterIcon: Bitmap? = null
    private var clusterColor: Int

    private var listener: MapViewListener? = null

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.DTNMapView)
        // Get bitmaps for the cluster
        var resId = a.getResourceId(R.styleable.DTNMapView_largeClusterIcon, 0)
        if (resId != 0) {
            largeClusterIcon = BitmapFactory.decodeResource(context.resources, resId)
        }

        resId = a.getResourceId(R.styleable.DTNMapView_smallClusterIcon, 0)
        if (resId != 0) {
            smallClusterIcon = BitmapFactory.decodeResource(context.resources, resId)
        }
        clusterColor = a.getColor(R.styleable.DTNMapView_clusterColor, ContextCompat.getColor(context, R.color.mapCluster))

        a.recycle()
    }

    fun setMapViewListener(listener: MapViewListener?) {
        this.listener = listener
    }

    override fun getMapAsync(callback: OnMapReadyCallback?) {
        super.getMapAsync({
            this.googleMap = it
            this.clusterManager = ClusterManager<StoreItem>(this.context, this.googleMap)
            this.clusterManager?.renderer = CustomIconRender(this.context, this.googleMap, this.clusterManager)
            this.clusterManager?.setOnClusterClickListener { this.onClusterClicked(it) }
            this.clusterManager?.setOnClusterItemClickListener { this.onMarkerClicked(it) }
            this.googleMap.setOnMarkerClickListener(this.clusterManager)
            this.googleMap.setOnCameraIdleListener { this.onCameraIdle() }
            this.googleMap.setOnMarkerClickListener(this.clusterManager)
            this.googleMap.setOnMapClickListener { this.onMapClicked(it) }
            callback?.onMapReady(googleMap)

            this.listener?.onMapLoaded()
        })
    }

    inner class CustomIconRender(context: Context?, map: GoogleMap?, clusterManager: ClusterManager<StoreItem>?)
        : DefaultClusterRenderer<StoreItem>(context, map, clusterManager) {

        val icon: Bitmap? = null
        val color: Int = clusterColor

        override fun getColor(clusterSize: Int): Int = this.color

        override fun onBeforeClusterItemRendered(item: StoreItem?, markerOptions: MarkerOptions?) {
            markerOptions?.icon(BitmapDescriptorFactory.fromBitmap(
                if (this@DTNMapView.lastClickedMarker?.equals(item) == true) {
                    largeClusterIcon
                } else {
                    smallClusterIcon
                }))
        }
    }

    private fun zoomToLatLng(latLng: LatLng, zoomLevel: Int = defaultZoom, animate: Boolean = true) {
        val center = CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel.toFloat())

        when(animate) {
            true -> this.googleMap.animateCamera(center)
            else -> this.googleMap.moveCamera(center)
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
        renderer?.getMarker(storeItem)?.setIcon(BitmapDescriptorFactory.fromBitmap(this.largeClusterIcon))
        this.lastClickedMarker = storeItem

        this.listener?.onMarkerClick(storeItem)

        return false
    }

    private fun setSmallIconForLastClickedMarker() {
        val renderer = this.clusterManager?.renderer as CustomIconRender?
        renderer?.getMarker(this.lastClickedMarker)?.setIcon(BitmapDescriptorFactory.fromBitmap(smallClusterIcon))
    }

    private fun onCameraIdle() {
        val latLngBounds = this.googleMap.projection.visibleRegion.latLngBounds

        val centerLatLng = latLngBounds.center
        val radius = latLngBounds.getRadiusInMiles()

        this.listener?.onCameraLocationChange(centerLatLng, Math.round(radius).toDouble())
        this.clusterManager?.onCameraIdle()
    }

    private fun onMapClicked(latLng: LatLng) {
        if (this.lastClickedMarker?.position?.equals(latLng) == false) {
            this.setSmallIconForLastClickedMarker()

            this.lastClickedMarker = null
            this.listener?.onMarkerDeselected()
        }
    }

    fun setStoreItems(stores: List<StoreItem>?) {
        if (stores?.contains(this.lastClickedMarker) == false) {
            this.lastClickedMarker = null
            this.listener?.onMarkerDeselected()
        }

        this.clusterManager?.clearItems()
        this.clusterManager?.addItems(stores)
        this.clusterManager?.cluster()
    }

    private fun LatLngBounds.getRadiusInMiles(): Double {
        val northEast = this.northeast
        val southWest = this.southwest

        val res = FloatArray(2)

        Location.distanceBetween(northEast.latitude, northEast.longitude, southWest.latitude, southWest.longitude, res)
        return (res[0] / 2).metersToMiles().roundTo(2)
    }

    interface MapViewListener {
        fun onMapLoaded()
        fun onCameraLocationChange(latNLng: LatLng, radius: Double = 0.0)
        fun onMarkerClick(storeItem: StoreItem?)
        fun onMarkerDeselected()
    }
}