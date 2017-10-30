package com.heb.dtn.account

import android.content.Context
import android.text.TextUtils
import com.heb.dtn.app.AppProxy
import com.heb.dtn.foundation.extension.formUnion
import com.heb.dtn.foundation.promise.android.always
import com.heb.dtn.foundation.promise.android.then
import com.heb.dtn.foundation.promise.android.thenp
import com.heb.dtn.foundation.security.CryptoService
import com.heb.dtn.foundation.service.JSONDecoder
import com.heb.dtn.foundation.service.JSONEncoder
import com.heb.dtn.service.api.AccountService
import com.heb.dtn.service.api.AccountServiceError
import com.heb.dtn.service.api.AccountValidationFlags
import com.heb.dtn.service.api.SSOService
import com.heb.dtn.service.domain.account.AuthSession
import com.heb.dtn.service.domain.profile.Profile
import com.heb.dtn.service.manager.DefaultDinnerTonightAccountServiceManager
import com.heb.dtn.service.manager.DinnerTonightAccountServiceManager
import com.heb.dtn.service.manager.DinnerTonightServiceEnvironment
import com.inmotionsoftware.promise.Promise

//
// Created by Khuong Huynh on 9/27/17.
//

class AccountManager(private val context: Context, private val environment: DinnerTonightServiceEnvironment, private val delegate: AccountManager.Delegate?) {

    interface Delegate {
        fun didAuthenticate(authSession: AuthSession)
        fun didUnauthenticate()
    }

    private enum class CrpAlias(val alias: String) {
        AccountAlias("com.heb.dtn.account")
    }

    val userProfile: Profile? get() = this.authSession?.profile

    private val accountServiceManager: DinnerTonightAccountServiceManager
    private var authSession: AuthSession? = null
    private var crpSlt: String = ""

    private val accountService: AccountService get() = this.accountServiceManager.accountService()
    private val ssoService: SSOService get() = this.accountServiceManager.ssoService()

    init {
        this.accountServiceManager = DefaultDinnerTonightAccountServiceManager(context = this.context, environment = this.environment)
    }

    fun authenticate(email: String, password: String): Promise<Unit> {
        this.accountServiceManager.setAuthSession(authSession = null)
        return this.ssoService
                .authenticate(email = email, password = password)
                .thenp { resp ->
                    this.accountService.getSessionToken(uid = resp.uid, signature = resp.uidSignature, timestamp = resp.timestamp)
                        .then { AuthSession(token = it, profile = resp.profile) }
                }
                .thenp {
                    // Always generate a new CrpSlt on new authentication
                    this.generateCryptoSalt()
                    this.didAuthenticate(authSession = it)
                }
    }

    fun reauthenticate(): Promise<Unit> {
        return Promise(Unit).thenp {
            val authSession = this.authSessionFromPreferences()
            if (authSession != null) {
                this.didAuthenticate(authSession = authSession)
            } else {
                this.unauthenticate()
            }
        }
    }

    fun unauthenticate(): Promise<Unit> {
        return Promise(Unit).then {
            this.clearAuthSession()
            this.authSession = null
            this.delegate?.didUnauthenticate()
            Unit
        }
    }

    fun isAuthenticated(): Boolean = (this.authSession != null)

    fun login(email: String, password: String): Promise<Unit> {
        return this.ssoService.authenticate(email = email, password = password)
                .thenp {
                    this.accountService.getSessionToken(uid = it.uid, signature = it.uidSignature, timestamp = it.timestamp)
                }
                .then {
                    // TODO: deal with jwt token
                }
    }

    fun createAccount(firstName: String, lastName: String, email: String, password: String): Promise<Unit> {
        val errors = AccountValidationFlags()
        if (firstName.isEmpty()) { errors.formUnion(AccountValidationFlags.firstName) }
        if (lastName.isEmpty()) { errors.formUnion(AccountValidationFlags.lastName) }
        if (password.isEmpty()) { errors.formUnion(AccountValidationFlags.password) }
        if (email.isEmpty()) { errors.formUnion(AccountValidationFlags.email) }
        if (!errors.isEmpty) return Promise(error = AccountServiceError.Validation(errors))

        return this.ssoService.registerAccount(email = email, password = password)
                .then { this.accountService.finalizeAccountRegistration(token = it, firstName = firstName, lastName = lastName) }
                .then { this.authenticate(email = email, password = password) }
                .asVoid()
    }

    fun forgotPassword(email: String): Promise<Unit> = this.ssoService.forgotPassword(email = email).asVoid()

    //
    // Private Methods
    //

    private fun didAuthenticate(authSession: AuthSession): Promise<Unit> {
        return Promise.void()
                .thenp {
                    this.saveAuthSessionToPreferences(authSession = authSession)
                    this.authSession = authSession
                    Promise.void()
                }
                .always {
                    this.accountServiceManager.setAuthSession(authSession = authSession)
                    this.delegate?.didAuthenticate(authSession = authSession)
                }
    }

    private fun generateCryptoSalt() {
        val crpSlt = CryptoService.generateSalt()
        this.crpSlt = crpSlt
        AppProxy.proxy.preferences().setCrpSlt(crpSlt)
    }

    private fun clearAuthSession() {
        val preferences = AppProxy.proxy.preferences()
        preferences.setSessionInfo(session = null)
        preferences.setCrpSlt("")

        this.authSession = null
        this.crpSlt = ""
        this.accountServiceManager.setAuthSession(authSession = null)
    }

    private fun authSessionFromPreferences(): AuthSession? {
        val preferences = AppProxy.proxy.preferences()
        if (this.crpSlt.isEmpty()) this.crpSlt = preferences.crpSlt()
        if (this.crpSlt.isEmpty()) return null

        val session = preferences.sessionInfo()
        return try {
            val key = CryptoService.getSecretKey(CrpAlias.AccountAlias.alias, this.crpSlt)
            val tokenStr = CryptoService.decrypt(key, session)
            JSONDecoder().decode<AuthSession>(tokenStr.toByteArray(Charsets.UTF_8))
        } catch (ignored: Exception) {
            null
        }
    }

    private fun saveAuthSessionToPreferences(authSession: AuthSession): Boolean {
        if (this.crpSlt.isEmpty()) return false

        try {
            val tokenData = JSONEncoder().encode(value = authSession)
            val key = CryptoService.getSecretKey(CrpAlias.AccountAlias.alias, this.crpSlt)
            val tokenStr = String(tokenData!!, Charsets.UTF_8)
            val session = CryptoService.encrypt(key, tokenStr)
            if (TextUtils.isEmpty(session)) return false
            AppProxy.proxy.preferences().setSessionInfo(session)
            return true
        } catch (ignored: Exception) {
            return false
        }
    }

}
