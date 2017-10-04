//
// Created by Khuong Huynh on 11/28/16.
//

package com.heb.dtn.locator

import android.content.Intent
import com.heb.dtn.R
import com.google.android.gms.location.LocationSettingsStates
import com.heb.dtn.foundation.promise.android.then
import com.heb.dtn.app.AppProxy
import com.heb.dtn.flow.core.BaseFlowActivity
import com.heb.dtn.locator.domain.StoreItem
import com.heb.dtn.utils.LocationServiceManager
import com.inmotionsoftware.imsflow.FlowPromise

class StoreLocatorActivity : BaseFlowActivity() {

    private var flow: FlowPromise<StoreItem>? = null

    override fun onStartFlow() {
        if (this.flow != null) return
        this.flow = AppProxy.proxy.flow.locateStore(this, this.fragmentContainerId, StoreLocatorOption.DEFAULT)
        this.flow!!.then { this.finish() }
    }

    override fun isDisplayHomeAsUpEnabled(): Boolean = true

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode) {
            LocationServiceManager.LOCATION_SETTINGS_RESULT_CODE -> {
                val fragment = this.supportFragmentManager.findFragmentById(R.id.fragmentContainer)

                if (fragment is LocationServiceManager.Listener) {
                    fragment.onLocationSettingsResult(resultCode, LocationSettingsStates.fromIntent(data))
                }
            }
        }
    }
}
