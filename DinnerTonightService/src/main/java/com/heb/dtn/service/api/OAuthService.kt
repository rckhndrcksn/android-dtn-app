package com.heb.dtn.service.api

import com.heb.dtn.service.domain.account.OAuthToken
import com.heb.dtn.service.domain.profile.UserId
import com.inmotionsoftware.promise.Promise

//
// Created by Khuong Huynh on 9/27/17.
//

interface OAuthService {

    fun authenticate(username: String, password: String): Promise<OAuthToken>

    fun authenticate(refreshToken: String): Promise<OAuthToken>

    fun unauthenticate(userId: UserId): Promise<Boolean>

}
