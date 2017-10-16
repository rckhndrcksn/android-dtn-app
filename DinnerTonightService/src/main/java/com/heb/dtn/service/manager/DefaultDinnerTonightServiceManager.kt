package com.heb.dtn.service.manager

import android.content.Context
import com.heb.dtn.foundation.service.*
import com.heb.dtn.service.api.*
import com.heb.dtn.service.domain.account.OAuthToken
import com.heb.dtn.service.internal.dtn.*
import com.heb.dtn.service.internal.dtn.mock.MockDinnerTonightProductService
import com.heb.dtn.service.internal.dtn.mock.MockDinnerTonightStoreService

//
// Created by Khuong Huynh on 9/22/17.
//

class DefaultDinnerTonightServiceManager(private val context: Context, private val environment: DinnerTonightServiceEnvironment)
    : DinnerTonightServiceManager {

    private val config: HTTPService.Config = HTTPService.Config(baseUrl = environment.baseUrl())

    init {
        this.config.isAlwaysTrustHost =
            when(environment) {
                DinnerTonightServiceEnvironment.Dev, DinnerTonightServiceEnvironment.Staging -> true
                else -> false
            }

        val decoders: MutableMap<String, Decoder> = mutableMapOf()

        // Custom mimeType for Json
        val jsonDecoder = JSONDecoder()
        decoders["application/vnd.spring-boot.actuator.v1+json;charset=UTF-8"] = jsonDecoder
        decoders["application/json;charset=UTF-8"] = jsonDecoder
        this.config.decoders = decoders

        val encoders: MutableMap<String, Encoder> = mutableMapOf()
        encoders["application/json"] = JSONEncoder()
        this.config.encoders = encoders
    }

    override fun serverInfoService(): ServerInfoService = DinnerTonightServerInfoService(config = this.config)

    override fun productService(): ProductService = DinnerTonightProductService(config = this.config)

    override fun storeService(): StoreService = DinnerTonightStoreService(config = this.config)

    override fun cartService(): CartService = DinnerTonightCartService(config = this.config)

    override fun setOAuthToken(token: OAuthToken?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun orderService(): OrderService = DinnerTonightOrderService(config = this.config)
}
