package com.heb.dtn.flow.core

import android.content.Context
import android.os.Bundle
import android.support.annotation.CallSuper
import android.view.*
import com.heb.dtn.R
import com.heb.dtn.utils.*

import com.inmotionsoftware.imsflow.FlowFragment

abstract class BaseFlowFragment<ARGS, RETURN>
    : FlowFragment<ARGS, RETURN>()
        , UIControlDelegate by UIControlDelegation() {

    open protected var titleResId: Int? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is UIControlDelegate) {
            this.controlActivity = context.controlActivity
            this.progressBar = context.progressBar
            this.fragmentContainerId = context.fragmentContainerId
            this.fragment = this
        }
    }

    @CallSuper
    override fun flowWillRun(args: ARGS) {
        this.titleResId?.let { this.activity.setTitle(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setHasOptionsMenu(true)
    }

    final override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val fragmentContainerView = inflater.inflate(R.layout.fragment_base_flow_default, container, false) as ViewGroup
        this.createView(inflater = inflater, container = container, savedInstanceState = savedInstanceState)?.let {
            fragmentContainerView.addView(it)
        }
        return fragmentContainerView
    }

    abstract fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?

    override fun onPause() {
        KeyboardUtils.dismissKeyboard(this.activity)
        super.onPause()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        menu?.clear()
    }

}
