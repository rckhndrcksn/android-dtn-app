package com.heb.dtn.utils

import android.os.Build

/**
 * Utils class used to check the OS version.
 */
class VersionUtils {
    companion object {
        /**
         * Returns true if the device is running Lollipop or greater.
         */
        fun isLollipopOrGreater(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
    }
}