package com.heb.dtn.locator

import android.location.Location
import com.google.android.gms.location.LocationSettingsStates
import com.heb.dtn.flow.core.BaseFlowFragment
import com.heb.dtn.utils.LocationServiceManager

abstract class LocatorFragment<ARGS, RETURN> : BaseFlowFragment<ARGS, RETURN>(), LocationServiceManager.Listener {

    protected var locationServiceManager: LocationServiceManager? = null

    /**
     * Must be called from the Fragment.onViewCreated() method
     */
    fun initializeLocator() { this.locationServiceManager = LocationServiceManager(this) }

    override fun onLocationRetrieved(location: Location, isLocationEnabled: Boolean) {}
    override fun onPermissionDenied() {}

    override fun onLocationSettingsResult(resultCode: Int, states: LocationSettingsStates) {
        this.locationServiceManager?.onLocationSettingsResult(states)
    }

    override fun onStop() {
        this.locationServiceManager?.onStop()
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
        this.locationServiceManager?.onStart()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode) {
            LocationServiceManager.PERMISSION_REQUEST_CODE -> { this.locationServiceManager?.onRequestPermissionResult() }
        }
    }
}
