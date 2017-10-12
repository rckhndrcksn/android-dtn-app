package com.heb.dtn.flow.core

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.widget.Toolbar
import android.widget.ProgressBar
import com.heb.dtn.R
import com.heb.dtn.utils.UIControlDelegate
import com.heb.dtn.utils.UIControlDelegation
import com.inmotionsoftware.imsflow.FlowActivity


abstract class BaseFlowActivity : FlowActivity(), UIControlDelegate by UIControlDelegation()  {

    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val toolbar = findViewById(R.id.toolbar) as Toolbar?
        toolbar?.let {
            setSupportActionBar(toolbar)

            val actionBar = this.supportActionBar
            actionBar?.let {
                it.elevation = 0f
                it.setDisplayHomeAsUpEnabled(this.isDisplayHomeAsUpEnabled())
            }
        }

        this.controlActivity = this
        this.progressBar = this.findViewById(R.id.spinner) as ProgressBar
        this.fragmentContainerId = R.id.fragmentContainer
    }

    open protected fun isDisplayHomeAsUpEnabled(): Boolean = true

    open protected fun hideActionBar() {
        this.supportActionBar?.hide()
    }
}
