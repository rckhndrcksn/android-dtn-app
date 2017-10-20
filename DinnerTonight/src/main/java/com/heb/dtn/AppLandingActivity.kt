package com.heb.dtn;

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.format.DateUtils
import android.widget.ProgressBar
import com.heb.dtn.app.AppProxy
import com.heb.dtn.extensions.hide
import com.heb.dtn.utils.UIControlDelegate
import com.heb.dtn.utils.UIControlDelegation
import com.inmotionsoftware.imsflow.*

class AppLandingActivity : FlowActivity(),  UIControlDelegate by UIControlDelegation() {

    private val delayInterval: Long = DateUtils.SECOND_IN_MILLIS * 2    // 2 secs
    private var flow: FlowPromise<Unit>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_app_landing)

        this.controlActivity = this
        this.progressBar = this.findViewById(R.id.spinner) as ProgressBar
        this.fragmentContainerId = R.id.fragmentContainer
    }

    override fun onStartFlow() {
        if (this.flow != null) return
        val activityIndicator = this.showActivityIndicator()
        Handler().postDelayed({
            activityIndicator?.hide()
            this.startFlow()
        }, this.delayInterval)
    }

    private fun startFlow() {
        this.flow = AppProxy.proxy.flow.appLanding(context = this, fragmentContainerView = R.id.fragmentContainer)
        this.flow!!.back { this.finishFlow() }
                .cancel { this.finishFlow() }
                .complete { this.finishFlow() }
    }

    private fun finishFlow() {
        this.startActivity(Intent(this, HomeActivity::class.java))
        this.finish()
    }

}
