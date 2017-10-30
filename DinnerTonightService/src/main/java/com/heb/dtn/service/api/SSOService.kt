package com.heb.dtn.service.api

import com.heb.dtn.service.domain.account.AuthResponse
import com.heb.dtn.service.domain.account.Registration
import com.inmotionsoftware.promise.Promise

//
// Created by Maksim Orlovich on 10/10/17.
//

interface SSOService {

    fun authenticate(email: String, password: String): Promise<AuthResponse>

    fun registerAccount(email: String, password: String): Promise<Registration>

    fun forgotPassword(email: String): Promise<Unit>

    fun resetPassword(resetToken: String, password: String): Promise<Unit>

}
