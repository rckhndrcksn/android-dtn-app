package com.heb.dtn.service.manager

import com.heb.dtn.foundation.service.HTTPService
import com.heb.dtn.service.domain.account.AuthSession

//
// Created by Khuong Huynh on 9/27/17.
//

interface DinnerTonightAuthServiceManager {

    fun setAuthSession(authSession: AuthSession?)

}

internal fun HTTPService.Config.setAuthSession(authSession: AuthSession?) {
    val authTokenHeaderName = "Authorization"
    val headers: MutableMap<String, String> = mutableMapOf()
    headers.putAll(this.headers)

    if (authSession != null) {
        headers[authTokenHeaderName] = "Bearer $authSession.token"
    } else {
        if (!headers.contains(authTokenHeaderName)) return
        headers.remove(authTokenHeaderName)
    }
    this.headers = headers
}
