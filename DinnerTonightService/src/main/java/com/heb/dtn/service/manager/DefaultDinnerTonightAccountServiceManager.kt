package com.heb.dtn.service.manager

import android.content.Context
import com.heb.dtn.foundation.service.Decoder
import com.heb.dtn.foundation.service.HTTPService
import com.heb.dtn.foundation.service.JSONDecoder
import com.heb.dtn.service.api.AccountService
import com.heb.dtn.service.api.OAuthService
import com.heb.dtn.service.domain.account.OAuthToken

//
// Created by Khuong Huynh on 9/27/17.
//

class DefaultDinnerTonightAccountServiceManager(private val context: Context, private val environment: DinnerTonightServiceEnvironment)
    : DinnerTonightAccountServiceManager {

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

        this.config.decoders = decoders
    }

    override fun accountService(): AccountService {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun oauthService(): OAuthService {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setOAuthToken(token: OAuthToken?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
