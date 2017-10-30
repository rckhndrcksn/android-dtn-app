package com.heb.dtn.app

import android.support.v7.app.AppCompatActivity
import com.heb.dtn.flow.account.CreateAccountFlowController
import com.heb.dtn.flow.account.LoginFlowController
import com.heb.dtn.flow.app.AppLandingFlowController
import com.heb.dtn.flow.fullfillment.FullfillmentFlowController
import com.heb.dtn.flow.fullfillment.LocateStoreFlowController
import com.heb.dtn.locator.StoreLocatorOption
import com.heb.dtn.locator.domain.StoreItem
import com.heb.dtn.widget.DTNTabLayout
import com.inmotionsoftware.imsflow.*

//
// Created by Khuong Huynh on 9/27/17.
//

class AppFlow {

    // App

    fun appLanding(context: AppCompatActivity, fragmentContainerView: Int): FlowPromise<Unit>
            = AppLandingFlowController(context = context, fragmentContainerView = fragmentContainerView).start()

    // Account

    fun login(context: AppCompatActivity, fragmentContainerView: Int): FlowPromise<Boolean>
        = LoginFlowController(context = context, fragmentContainerView = fragmentContainerView).start()

    fun createAccount(context: AppCompatActivity, fragmentContainerView: Int): FlowPromise<Boolean>
            = CreateAccountFlowController(context = context, fragmentContainerView = fragmentContainerView).start()

    // Fullfillment

    fun fullfillment(context: AppCompatActivity, fragmentContainerView: Int, tabLayout: DTNTabLayout): FlowPromise<StoreItem>
            = FullfillmentFlowController(context = context, fragmentContainerView = fragmentContainerView, tabLayout = tabLayout).start()

    fun pickupFullfillment(context: AppCompatActivity, fragmentContainerView: Int, option: StoreLocatorOption): FlowPromise<StoreItem>
            = LocateStoreFlowController(context = context, fragmentContainerView = fragmentContainerView, option = option).start()
}
