package com.heb.dtn.service.manager

import android.content.Context
import com.heb.dtn.foundation.service.HTTPService
import com.heb.dtn.service.api.*
import com.heb.dtn.service.domain.account.AuthSession
import com.heb.dtn.service.internal.dtn.*

//
// Created by Khuong Huynh on 9/22/17.
//

class DefaultDinnerTonightServiceManager(private val context: Context, private val environment: DinnerTonightServiceEnvironment)
    : DinnerTonightServiceManager {

    private val config = HTTPService.Config(baseUrl = this.environment.baseUrl)
    private  val authConfig = HTTPService.Config(baseUrl = this.environment.baseUrl)

    init {
        this.config.isAlwaysTrustHost =
            when(this.environment) {
                DinnerTonightServiceEnvironment.Dev, DinnerTonightServiceEnvironment.Staging -> true
                else -> false
            }
        this.authConfig.isAlwaysTrustHost = this.config.isAlwaysTrustHost
    }

    override fun serverInfoService(): ServerInfoService = DinnerTonightServerInfoService(config = this.config)

    override fun productService(): ProductService = DinnerTonightProductService(config = this.config)

    override fun storeService(): StoreService = DinnerTonightStoreService(config = this.config)

    override fun cartService(): CartService = DinnerTonightCartService(config = this.authConfig)

    override fun orderService(): OrderService = DinnerTonightOrderService(config = this.authConfig)

    override fun setAuthSession(authSession: AuthSession?) {
        this.authConfig.setAuthSession(authSession = authSession)
    }

}
