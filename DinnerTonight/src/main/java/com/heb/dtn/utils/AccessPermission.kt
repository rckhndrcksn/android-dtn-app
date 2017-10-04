//
// Created by Khuong Huynh on 12/1/16.
//

package com.heb.dtn.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat

import com.heb.dtn.app.AppProxy;

object AccessPermission {

    val isLocationAccessGranted: Boolean
        get() = AccessPermission.isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION) || AccessPermission.isPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION)

    val isCameraAccessGranted: Boolean
        get() = AccessPermission.isPermissionGranted(Manifest.permission.CAMERA)

    fun requestLocationAccessPermission(activity: Activity, requestCode: Int?) {
        AccessPermission.requestAccessPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION, requestCode)
    }

    fun requestCameraAccessPermission(activity: Activity, requestCode: Int?) {
        AccessPermission.requestAccessPermission(activity, Manifest.permission.CAMERA, requestCode)
    }

    private fun isPermissionGranted(permission: String): Boolean {
        val context = AppProxy.Companion.proxy.getApplicationContext()
        return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestAccessPermission(activity: Activity, permission: String, requestCode: Int?) {
        var requestCode = requestCode
        if (requestCode == null) {
            requestCode = 1
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            ActivityCompat.requestPermissions(activity, arrayOf(permission), requestCode)
        } else {
            // Contact permissions have not been granted yet. Request them directly.
            ActivityCompat.requestPermissions(activity, arrayOf(permission), requestCode)
        }
    }

}
/** No instances  */
