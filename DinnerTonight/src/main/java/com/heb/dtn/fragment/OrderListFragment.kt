package com.heb.dtn.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.heb.dtn.R
import com.heb.dtn.flow.core.BaseFlowFragment

/**
 * Created by jcarbo on 10/18/17.
 */
class OrderListFragment : BaseFlowFragment<Unit, Unit>() {

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_order_list, container, false)
    }
}