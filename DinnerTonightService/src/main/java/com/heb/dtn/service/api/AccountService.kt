package com.heb.dtn.service.api

import com.heb.dtn.service.domain.account.Registration
import com.heb.dtn.service.domain.account.Session
import com.heb.dtn.service.domain.account.UID
import com.heb.dtn.service.domain.profile.Profile
import com.inmotionsoftware.promise.Promise
import java.util.*

//
// Created by Khuong Huynh on 9/27/17.
//

class AccountValidationFlags : BitSet {

    constructor(rawValue: Int? = null) : super() {
        rawValue?.let { this.set(it) }
    }

    constructor(flags: Array<AccountValidationFlags>) : super() {
        flags.forEach { this.or(it) }
    }

    companion object {
        val emailInUse = AccountValidationFlags(rawValue = 1 shl 1)
        val email      = AccountValidationFlags(rawValue = 1 shl 2)
        val firstName  = AccountValidationFlags(rawValue = 1 shl 3)
        val lastName   = AccountValidationFlags(rawValue = 1 shl 4)
        val password   = AccountValidationFlags(rawValue = 1 shl 5)
    }

}

sealed class AccountServiceError : Throwable() {
    class Validation(val flags: AccountValidationFlags): AccountServiceError()
    class Network: AccountServiceError()
    class Unknown: AccountServiceError()
}

interface AccountService {

    fun resetPassword(email: String): Promise<Unit>

    fun finalizeAccount(token: Registration, firstName: String, lastName: String): Promise<Session>

    fun getProfile(): Promise<Profile>

}
