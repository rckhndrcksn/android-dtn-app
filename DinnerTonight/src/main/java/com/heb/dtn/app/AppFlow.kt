package com.heb.dtn.app

import android.support.v7.app.AppCompatActivity
import com.heb.dtn.flow.*
import com.heb.dtn.flow.account.CreateAccountFlowController
import com.heb.dtn.flow.app.AppLandingFlowController
import com.heb.dtn.locator.StoreLocatorOption
import com.heb.dtn.locator.domain.StoreItem
import com.inmotionsoftware.imsflow.*

//
// Created by Khuong Huynh on 9/27/17.
//

class AppFlow {

    // App

    fun appLanding(context: AppCompatActivity, fragmentContainerView: Int): FlowPromise<AppLandingFlowController.Result>
            = AppLandingFlowController(context = context, fragmentContainerView = fragmentContainerView).start()

    // Account

    fun createAccount(context: AppCompatActivity, fragmentContainerView: Int): FlowPromise<Boolean>
            = CreateAccountFlowController(context = context, fragmentContainerView = fragmentContainerView).start()

    // Store Locator

    fun locateStore(context: AppCompatActivity, fragmentContainerView: Int, option: StoreLocatorOption): FlowPromise<StoreItem>
            = LocateStoreFlowController(context = context, fragmentContainerView = fragmentContainerView, option = option).start()

}
