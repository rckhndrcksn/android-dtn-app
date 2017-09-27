package com.heb.dtn.service.manager

import com.heb.dtn.service.domain.account.OAuthToken

//
// Created by Khuong Huynh on 9/27/17.
//

public interface DinnerTonightOAuthServiceManager {

    fun setOAuthToken(token: OAuthToken?)

}
