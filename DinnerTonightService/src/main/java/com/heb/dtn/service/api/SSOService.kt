package com.heb.dtn.service.api

import com.heb.dtn.service.domain.account.Registration
import com.inmotionsoftware.promise.Promise

//
// Created by Maksim Orlovich on 10/10/17.
//

typealias RegistrationToken = String
typealias UID = String

interface SSOService {

    fun initializeAccount(): Promise<RegistrationToken>

    fun registerAccount(regToken: RegistrationToken, email: String, password: String): Promise<Registration>

}
