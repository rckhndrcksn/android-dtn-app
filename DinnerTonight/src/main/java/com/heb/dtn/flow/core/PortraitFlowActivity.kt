package com.heb.dtn.flow.core

import android.content.pm.ActivityInfo
import android.os.Bundle
import com.inmotionsoftware.imsflow.FlowActivity

/**
 * Abstract activity that locks the orientation to Portrait mode.
 */
abstract class PortraitFlowActivity: FlowActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Lock the orientation to portrait mode
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
    }
}