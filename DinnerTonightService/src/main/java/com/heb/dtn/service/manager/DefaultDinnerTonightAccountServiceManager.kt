package com.heb.dtn.service.manager

import android.content.Context
import com.heb.dtn.foundation.service.HTTPService
import com.heb.dtn.foundation.service.JSONDecoder
import com.heb.dtn.service.api.AccountService
import com.heb.dtn.service.api.OAuthService
import com.heb.dtn.service.api.SSOService
import com.heb.dtn.service.domain.DTNJSONAdapter
import com.heb.dtn.service.domain.account.OAuthToken
import com.heb.dtn.service.internal.dtn.DinnerTonightAccountService
import com.heb.dtn.service.internal.dtn.GigyaConfig
import com.heb.dtn.service.internal.dtn.GigyaSSOService

//
// Created by Khuong Huynh on 9/27/17.
//

class DefaultDinnerTonightAccountServiceManager(private val context: Context, private val environment: DinnerTonightServiceEnvironment)
    : DinnerTonightAccountServiceManager {

    private val config: HTTPService.Config = HTTPService.Config(baseUrl = this.environment.baseUrl)
    private val gigyaServiceConfig: HTTPService.Config = HTTPService.Config(baseUrl = this.environment.gigya.baseUrl)
    private val gigyaConfig: GigyaConfig = GigyaConfig(config = this.gigyaServiceConfig
                                                        , apiKey = this.environment.gigya.apiKey
                                                        , userKey = this.environment.gigya.userKey
                                                        , secret = this.environment.gigya.secret)

    init {
        this.config.isAlwaysTrustHost =
            when (this.environment) {
                DinnerTonightServiceEnvironment.Dev, DinnerTonightServiceEnvironment.Staging -> true
                else -> false
            }

        this.gigyaServiceConfig.isAlwaysTrustHost = this.config.isAlwaysTrustHost
        this.gigyaServiceConfig.decoders = mapOf(
                Pair(HTTPService.MimeType.Json.value, JSONDecoder(arrayOf(DTNJSONAdapter()))))
    }

    override fun accountService(): AccountService = DinnerTonightAccountService(config = this.config)

    override fun ssoService(): SSOService = GigyaSSOService(gigya = this.gigyaConfig)

    override fun oauthService(): OAuthService {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setOAuthToken(token: OAuthToken?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
