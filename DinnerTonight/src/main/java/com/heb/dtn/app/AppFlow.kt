package com.heb.dtn.app

import android.support.v7.app.AppCompatActivity
import com.heb.dtn.flow.*
import com.heb.dtn.locator.StoreLocatorOption
import com.heb.dtn.locator.domain.StoreItem
import com.inmotionsoftware.imsflow.*

//
// Created by Khuong Huynh on 9/27/17.
//

class AppFlow {

    fun locateStore(context: AppCompatActivity, fragmentContainerView: Int, option: StoreLocatorOption): FlowPromise<StoreItem>
            = LocateStoreFlowController(context = context, fragmentContainerView = fragmentContainerView, option = option).start()

}
