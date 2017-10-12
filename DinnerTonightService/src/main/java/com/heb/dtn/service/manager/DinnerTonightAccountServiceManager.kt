package com.heb.dtn.service.manager

import com.heb.dtn.service.api.AccountService
import com.heb.dtn.service.api.OAuthService
import com.heb.dtn.service.api.SSOService

//
// Created by Khuong Huynh on 9/27/17.
//

public interface DinnerTonightAccountServiceManager : DinnerTonightOAuthServiceManager {

    fun accountService(): AccountService

    fun ssoService(): SSOService

    fun oauthService(): OAuthService

}
