package com.heb.dtn.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.Fragment
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
//import com.heb.pharmacy.app.AppProxy
import java.util.concurrent.TimeUnit

class LocationServiceManager(val supportFragment: Fragment) {
    interface Listener {
        fun onLocationSettingsResult(resultCode: Int, states: LocationSettingsStates)
        fun onLocationRetrieved(location: Location, isLocationEnabled: Boolean)
        fun onPermissionDenied()
    }

    companion object {
        val LOCATION_SETTINGS_RESULT_CODE = 111
        val PERMISSION_REQUEST_CODE = 121
    }

    private var googleApiClient: GoogleApiClient? = null
    private var requestingPermission = false
    private var listener: Listener? = null

    init {
        setupClient()

        if (this.supportFragment is Listener) {
            this.listener = this.supportFragment
        }
    }

    fun onStop() {
        this.googleApiClient?.disconnect()
    }

    fun onStart() {
        if (!AccessPermission.isLocationAccessGranted) {
            this.requestingPermission = true
            this.supportFragment.requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_CODE)
        }
    }

    fun onRequestPermissionResult() {
        this.requestingPermission = false
        when(AccessPermission.isLocationAccessGranted) {
            true -> this.requestOneShotLocationUpdate()
            else -> this.notifyDefaultLocation()
        }
    }

    fun onLocationSettingsResult(states: LocationSettingsStates) {
        when(states.isLocationUsable) {
            true -> this.refreshUserLocation()
            else -> this.notifyDefaultLocation()
        }
    }

    fun requestOneShotLocationUpdate() {
        if (this.googleApiClient?.isConnected == false) {
            this.googleApiClient?.connect()
        } else if (this.canRefreshLocation()) {
            this.refreshUserLocation()
        }
    }

    //
    // Private
    //
    private fun notifyDefaultLocation() {
        //val defaultStore = AppProxy.proxy().defaultCenterMapStore
        val location = Location("")
        //location.latitude = defaultStore.latitude ?: 0.0
        //location.longitude = defaultStore.longitude ?: 0.0
        location.latitude = 29.4192143
        location.longitude = -98.4966322

        this.listener?.onLocationRetrieved(location, false)
    }

    private fun refreshUserLocation() {
        LocationServices.FusedLocationApi.requestLocationUpdates(this.googleApiClient, buildLocationRequest(), object: LocationListener {
            override fun onLocationChanged(location: Location?) {
                location?.let {
                    LocationServices.FusedLocationApi.removeLocationUpdates(this@LocationServiceManager.googleApiClient, this)
                    this@LocationServiceManager.listener?.onLocationRetrieved(location, true)
                }
            }
        })
    }

    private fun canRefreshLocation(): Boolean {
        if (!AccessPermission.isLocationAccessGranted) {
            if (!this.requestingPermission) {
                this.listener?.onPermissionDenied()
            }

            return false
        } else if (!this.isLocationProvided(this.supportFragment.activity)) {
            // Dialog screen
            val result = LocationServices.SettingsApi.checkLocationSettings(this.googleApiClient, LocationSettingsRequest.Builder()
                                    .addLocationRequest(this.buildLocationRequest())
                                    .setAlwaysShow(true)
                                    .build())

            result.setResultCallback({ resultCallback ->
                val status = resultCallback.status
                when (status.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        // Location settings are not satisfied. But could be fixed by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result in onActivityResult().
                            status.startResolutionForResult(this.supportFragment.activity, LocationServiceManager.LOCATION_SETTINGS_RESULT_CODE)
                        } catch (ignore: IntentSender.SendIntentException) {
                            // If the resolution intent has been canceled or is no longer able to execute the request.
                            // Ignore the error.
                        }
                    }
                }
            }, 10, TimeUnit.SECONDS)

            return false
        }

        return true
    }

    private fun setupClient() {
        this.googleApiClient = GoogleApiClient.Builder(this.supportFragment.activity)
                .addConnectionCallbacks(object : GoogleApiClient.ConnectionCallbacks {
                    override fun onConnected(bundle: Bundle?) {
                        requestOneShotLocationUpdate()
                    }

                    override fun onConnectionSuspended(i: Int) {
                        this@LocationServiceManager.googleApiClient?.connect()
                    }
                })
                .addOnConnectionFailedListener {
                    // TODO: Show error message?
                }
                .addApi(LocationServices.API)
                .build()
    }

    private fun buildLocationRequest(): LocationRequest {
        val request = LocationRequest()
        request.interval = 10000
        request.fastestInterval = 5000
        request.setExpirationDuration(10000)
        request.numUpdates = 1
        request.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY

        return request
    }

    private fun isLocationProvided(activity: Activity): Boolean {
        val lm = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER) || lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
}
