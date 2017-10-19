//
// Created by Khuong Huynh on 11/28/16.
//

package com.heb.dtn.locator

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ProgressBar
import com.heb.dtn.R
import com.google.android.gms.location.LocationSettingsStates
import com.heb.dtn.HomeActivity
import com.heb.dtn.app.AppProxy
import com.heb.dtn.locator.domain.StoreItem
import com.heb.dtn.utils.LocationServiceManager
import com.heb.dtn.utils.UIControlDelegate
import com.heb.dtn.utils.UIControlDelegation
import com.inmotionsoftware.imsflow.*

class StoreLocatorActivity : FlowActivity(), UIControlDelegate by UIControlDelegation() {

    private var flow: FlowPromise<StoreItem>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_store_locator)

        this.controlActivity = this
        this.progressBar = this.findViewById(R.id.spinner) as ProgressBar
        this.fragmentContainerId = R.id.fragmentContainer
    }

    override fun onStartFlow() {
        if (this.flow != null) return
        this.flow = AppProxy.proxy.flow.locateStore(this, this.fragmentContainerId, StoreLocatorOption.DEFAULT)
        this.flow!!.back { this.finishFlow(result = Activity.RESULT_CANCELED) }
                .cancel { this.finishFlow(result = Activity.RESULT_CANCELED) }
                .complete { this.finishFlow(result = Activity.RESULT_OK) }
    }

    private fun finishFlow(result: Int) {
        val intent = Intent(this, HomeActivity::class.java)
        this.startActivity(intent)

        this.setResult(result)
        this.finish()
    }

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
