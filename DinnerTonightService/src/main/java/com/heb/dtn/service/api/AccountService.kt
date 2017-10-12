package com.heb.dtn.service.api

import com.heb.dtn.service.domain.account.Registration
import com.heb.dtn.service.domain.account.Session
import com.heb.dtn.service.domain.profile.Profile
import com.inmotionsoftware.promise.Promise

//
// Created by Khuong Huynh on 9/27/17.
//

sealed class AccountServiceError : Throwable() {
    class EmailAlreadyInUse(): AccountServiceError()
    class InvalidEmail(): AccountServiceError()
    class InvalidFirstName(): AccountServiceError()
    class InvalidLastName(): AccountServiceError()
    class InvalidPassword(): AccountServiceError()
    class Network(): AccountServiceError()
    class Unknown(): AccountServiceError()
}

interface AccountService {

    fun resetPassword(email: String): Promise<Unit>

    fun finalizeAccount(token: Registration, firstName: String, lastName: String): Promise<Session>

    fun getProfile(): Promise<Profile>

}
