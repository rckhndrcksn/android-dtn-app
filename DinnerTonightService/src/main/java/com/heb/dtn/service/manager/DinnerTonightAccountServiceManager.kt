package com.heb.dtn.service.manager

import com.heb.dtn.service.api.AccountService
import com.heb.dtn.service.api.OAuthService

//
// Created by Khuong Huynh on 9/27/17.
//

public interface DinnerTonightAccountServiceManager : DinnerTonightOAuthServiceManager {

    fun accountService(): AccountService

    fun oauthService(): OAuthService

}
