//
// Created by Khuong Huynh on 3/6/17.
//

package com.heb.dtn.app

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.heb.dtn.R
import com.heb.dtn.foundation.widget.recyclerview.RecyclerListView
import com.heb.dtn.flow.core.BaseFlowFragment
import com.heb.dtn.extensions.hide
import com.heb.dtn.extensions.show

interface RecyclerList<T> {
    enum class ActionType {
        DEFAULT, DEFAULT_WITH_FLOATING, POSITIVE_NEGATIVE, NO_ACTION, NO_ACTION_WITH_HEADER
    }

    interface RecyclerListControl<T> {
        fun onDefaultAction(view: View?) {}
        fun onPositiveAction(view: View?) {}
        fun onNegativeAction(view: View?) {}
        fun onFloatingAction(view: View?) {}
        fun showPlaceholderViews(): Boolean = false
        fun backgroundColor(): Int = Color.TRANSPARENT

        // Required
        fun onInitView()
        fun actionType(): ActionType
        fun onCreateAdapter(data: T): RecyclerListView.Adapter?
    }

    var listView: RecyclerListView?
    var controller: RecyclerListControl<T>?
    var defaultActionButton: Button?
    var negativeActionButton: Button?
    var positiveActionButton: Button?
    var floatingActionButton: FloatingActionButton?
    var placeHolderImage: ImageView?
    var placeHolderText: TextView?
    var adapterData: T?

    fun createView(inflater: LayoutInflater?, container: ViewGroup?, controller: RecyclerListControl<T>): View? {
        this.controller = controller
        val layout = when (controller.actionType()) {
            RecyclerList.ActionType.DEFAULT_WITH_FLOATING -> R.layout.fragment_list_view_default_with_floating_action
            RecyclerList.ActionType.POSITIVE_NEGATIVE -> R.layout.fragment_list_view_positive_negative_actions
            RecyclerList.ActionType.NO_ACTION -> R.layout.fragment_list_view_no_action_with_placeholders
            RecyclerList.ActionType.NO_ACTION_WITH_HEADER -> R.layout.fragment_list_view_no_action_with_header
            else -> R.layout.fragment_list_view_default_action
        }
        val rootView = inflater?.inflate(layout, container, false)
        this.listView = rootView?.findViewById(R.id.list_view) as? RecyclerListView
        this.defaultActionButton = rootView?.findViewById(R.id.action_button) as? Button
        this.negativeActionButton = rootView?.findViewById(R.id.negative_action_button) as? Button
        this.positiveActionButton = rootView?.findViewById(R.id.positive_action_button) as? Button
        this.floatingActionButton = rootView?.findViewById(R.id.floating_action_button) as? FloatingActionButton
        this.placeHolderImage = rootView?.findViewById(R.id.placeholderImage) as? ImageView
        this.placeHolderText = rootView?.findViewById(R.id.placeholderText) as? TextView

        this.defaultActionButton?.setOnClickListener { v -> controller.onDefaultAction(v) }
        this.negativeActionButton?.setOnClickListener { v -> controller.onNegativeAction(v) }
        this.positiveActionButton?.setOnClickListener { v -> controller.onPositiveAction(v) }
        this.floatingActionButton?.setOnClickListener { v -> controller.onFloatingAction(v) }

        controller.onInitView()
        rootView?.setBackgroundColor(controller.backgroundColor())
        this.adapterData?.let { this.updateListAdapter(it) }

        return rootView
    }

    fun handlePlaceHolderViews(show: Boolean) {
        this.listView ?: return

        if (show) { this.listView?.hide(remove = true) }
        else { this.listView?.show() }

        if (show) { this.placeHolderImage?.show() }
        else { this.placeHolderImage?.hide(remove = true) }

        if (show) { this.placeHolderText?.show() }
        else { this.placeHolderText?.hide(remove = true) }
    }

    fun setData(data: T) {
        this.adapterData = data
        this.updateListAdapter(data)
        this.handlePlaceHolderViews(this.controller?.showPlaceholderViews() ?: false)
    }

    fun updateListAdapter(data: T) {
        if (data == null || this.listView == null) return
        val adapter = this.controller?.onCreateAdapter(data)
        adapter?.let { this.listView?.setListAdapter(it) }
    }
}

class RecyclerListImpl<T>: RecyclerList<T> {
    override var listView: RecyclerListView? = null
    override var defaultActionButton: Button? = null
    override var negativeActionButton: Button? = null
    override var positiveActionButton: Button? = null
    override var floatingActionButton: FloatingActionButton? = null
    override var placeHolderImage: ImageView? = null
    override var placeHolderText: TextView? = null
    override var adapterData: T? = null
    override var controller: RecyclerList.RecyclerListControl<T>? = null
}

abstract class RecyclerListViewFlowFragment<T, ARGS, RETURN> :
        BaseFlowFragment<ARGS, RETURN>()
        , RecyclerList.RecyclerListControl<T>
        , RecyclerList<T> by RecyclerListImpl<T>(){

    final override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
            =  this.createView(inflater, container, this)
}

abstract class RecyclerListViewFragment<T> :
        Fragment()
        , RecyclerList.RecyclerListControl<T>
        , RecyclerList<T> by RecyclerListImpl<T>() {

    final override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View?
            = this.createView(inflater, container, this)
}