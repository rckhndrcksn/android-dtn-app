package com.heb.dtn.service.internal.dtn

import com.heb.dtn.foundation.service.HTTPService
import com.heb.dtn.foundation.service.post
import com.heb.dtn.service.api.AccountService
import com.heb.dtn.service.api.AccountServiceError
import com.heb.dtn.service.domain.account.Registration
import com.heb.dtn.service.domain.profile.Profile
import com.inmotionsoftware.promise.Promise

//
// Created by Maksim Orlovich on 10/11/17.
//

private class Register(val regToken: String, val profile: Profile)

class DinnerTonightAccountService(config: HTTPService.Config) : HTTPService(config = config), AccountService {

    override fun resetPassword(email: String): Promise<Unit> = Promise(error = AccountServiceError.Unknown())

    override fun finalizeAccount(token: Registration, firstName: String, lastName: String): Promise<com.heb.dtn.service.domain.account.Session> {
        val route = "api/v1/customer/register"
        val profile = Profile(userId = token.uid, firstName = firstName, lastName = lastName, email = token.email)
        val reg = Register(regToken = token.token, profile = profile)

        return this.post(route = route, body = UploadBody.Json(reg), type = com.heb.dtn.service.domain.account.Session::class.java)
    }

    override fun getProfile(): Promise<Profile> = Promise(error = HTTPService.Error.Generic())

}
