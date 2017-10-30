package com.heb.dtn.service.manager

import android.content.Context
import com.heb.dtn.foundation.service.HTTPService
import com.heb.dtn.foundation.service.JSONDecoder
import com.heb.dtn.service.api.AccountService
import com.heb.dtn.service.api.SSOService
import com.heb.dtn.service.domain.DTNJSONAdapter
import com.heb.dtn.service.domain.account.AuthSession
import com.heb.dtn.service.internal.dtn.DinnerTonightAccountService
import com.heb.dtn.service.internal.dtn.GigyaConfig
import com.heb.dtn.service.internal.dtn.GigyaSSOService

//
// Created by Khuong Huynh on 9/27/17.
//

class DefaultDinnerTonightAccountServiceManager(private val context: Context, private val environment: DinnerTonightServiceEnvironment)
    : DinnerTonightAccountServiceManager {

    private val authConfig: HTTPService.Config = HTTPService.Config(baseUrl = this.environment.baseUrl)
    private val gigyaServiceConfig: HTTPService.Config = HTTPService.Config(baseUrl = this.environment.gigya.baseUrl)
    private val gigyaConfig: GigyaConfig = GigyaConfig(config = this.gigyaServiceConfig
                                                        , apiKey = this.environment.gigya.apiKey
                                                        , userKey = this.environment.gigya.userKey
                                                        , secret = this.environment.gigya.secret)

    init {
        this.authConfig.isAlwaysTrustHost =
            when (this.environment) {
                DinnerTonightServiceEnvironment.Dev, DinnerTonightServiceEnvironment.Staging -> true
                else -> false
            }

        this.gigyaServiceConfig.isAlwaysTrustHost = this.authConfig.isAlwaysTrustHost
        this.gigyaServiceConfig.decoders = mapOf(
                Pair(HTTPService.MimeType.Json.value, JSONDecoder(arrayOf(DTNJSONAdapter()))))
    }

    override fun accountService(): AccountService = DinnerTonightAccountService(config = this.authConfig)

    override fun ssoService(): SSOService = GigyaSSOService(gigya = this.gigyaConfig)

    override fun setAuthSession(authSession: AuthSession?) {
        this.authConfig.setAuthSession(authSession = authSession)
    }

}
