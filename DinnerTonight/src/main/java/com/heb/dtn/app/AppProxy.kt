package com.heb.dtn.app

import android.support.multidex.BuildConfig
import android.support.multidex.MultiDexApplication
import com.heb.dtn.account.AccountManager
import com.heb.dtn.foundation.promise.android.always
import com.heb.dtn.service.domain.account.OAuthToken
import com.heb.dtn.service.domain.store.Store
import com.heb.dtn.service.manager.DefaultDinnerTonightServiceManager
import com.heb.dtn.service.manager.DinnerTonightServiceEnvironment
import com.heb.dtn.service.manager.DinnerTonightServiceManager
import com.inmotionsoftware.promise.Promise

//
// Created by Khuong Huynh on 9/27/17.
//

open class AppProxy : MultiDexApplication(), AccountManager.Delegate {

    lateinit private var serviceManager: DinnerTonightServiceManager
    lateinit private var accountManager: AccountManager
    lateinit private var preferences: AppPreferences
    lateinit private var environment: DinnerTonightServiceEnvironment
    val flow: AppFlow = AppFlow()

    /**
     * The default store to use for centering the map when location access permission is not granted.
     */
    lateinit var defaultCenterMapStore: Store

    companion object {
        lateinit var proxy: AppProxy
    }

    override fun onCreate() {
        super.onCreate()

        val context = applicationContext
//        this.environment = if (BuildConfig.DEBUG) DinnerTonightServiceEnvironment.Staging else DinnerTonightServiceEnvironment.Release
        this.environment = DinnerTonightServiceEnvironment.Dev

        this.defaultCenterMapStore = Store.defaultStore

        this.serviceManager = DefaultDinnerTonightServiceManager(context = context, environment = this.environment)
        this.preferences = AppPreferences(context)
        AppProxy.proxy = this

        initialize()
    }

    fun accountManager(): AccountManager = this.accountManager

    fun serviceManager(): DinnerTonightServiceManager = this.serviceManager

    fun preferences(): AppPreferences = this.preferences

    private fun initialize(): Promise<Boolean> {
        return Promise{ resolve, reject ->
            this.accountManager = AccountManager(this.applicationContext, this.environment, this)

            // Don't propagate error upstream.
            this.accountManager.reauthenticate().always { resolve(true) }
        }
    }

    //
    // MARK: AccountManager.Delegate
    //

    override fun didAuthenticate(token: OAuthToken) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun didUnauthenticate() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
