//
// Created by Khuong Huynh on 12/1/16.
//

package com.heb.dtn.utils

import android.Manifest
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import com.heb.dtn.app.AppProxy

object AccessPermission {

    val isLocationAccessGranted: Boolean
        get() {
            if (!VersionUtils.isMarshmallowOrGreater()) return true

            return AccessPermission.isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)
        }

    private fun isPermissionGranted(permission: String): Boolean {
        val context = AppProxy.Companion.proxy.applicationContext
        return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

}
/** No instances  */
