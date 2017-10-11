package com.heb.dtn.utils

import android.os.Build

/**
 * Utils class used to check the OS version.
 */
class VersionUtils {
    companion object {
        /**
         * Returns true if the device is running Marshmallow or greater.
         */
        fun isMarshmallowOrGreater(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }
}