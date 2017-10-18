package com.heb.dtn.service.api

import com.heb.dtn.service.domain.account.LoginResponse
import com.heb.dtn.service.domain.account.Registration
import com.heb.dtn.service.domain.account.RegistrationToken
import com.inmotionsoftware.promise.Promise

//
// Created by Maksim Orlovich on 10/10/17.
//

interface SSOService {

    fun forgotPassword(email: String): Promise<Unit>

    fun resetPassword(resetToken: String, password: String): Promise<Unit>

    fun login(email: String, password: String): Promise<LoginResponse>

    fun initializeAccount(): Promise<RegistrationToken>

    fun registerAccount(regToken: RegistrationToken, email: String, password: String): Promise<Registration>

}
