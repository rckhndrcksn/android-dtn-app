package com.heb.dtn.account

import android.content.Context
import android.text.TextUtils
import com.heb.dtn.app.AppProxy
import com.heb.dtn.foundation.promise.android.*
import com.heb.dtn.foundation.security.CryptoService
import com.heb.dtn.foundation.service.JSONDecoder
import com.heb.dtn.foundation.service.JSONEncoder
import com.heb.dtn.service.api.AccountService
import com.heb.dtn.service.api.OAuthService
import com.heb.dtn.service.domain.account.OAuthToken
import com.heb.dtn.service.domain.profile.Profile
import com.heb.dtn.service.domain.profile.forms.Registration
import com.heb.dtn.service.domain.profile.forms.UpdateProfile
import com.heb.dtn.service.manager.DefaultDinnerTonightAccountServiceManager
import com.heb.dtn.service.manager.DinnerTonightAccountServiceManager
import com.heb.dtn.service.manager.DinnerTonightServiceEnvironment
import com.heb.dtn.utils.DateFormatUtils
import com.inmotionsoftware.promise.Promise
import java.util.*

//
// Created by Khuong Huynh on 9/27/17.
//

internal fun Profile.update(form: UpdateProfile) {
}

class AccountManager(private val context: Context, private val environment: DinnerTonightServiceEnvironment, private val delegate: AccountManager.Delegate?) {

    interface Delegate {
        fun didAuthenticate(token: OAuthToken)
        fun didUnauthenticate()
    }

    private enum class CrpAlias(val alias: String) {
        AccountAlias("com.heb.pharmacy.account")
    }

    val userProfile: Profile?
        get() {
            if (this.profile == null) return null
            return this.profile
        }

    private val accountServiceManager: DinnerTonightAccountServiceManager
    private var authToken: OAuthToken? = null
    private var profile: Profile? = null
    private var crpSlt: String = ""

    private val oauthService: OAuthService get() = this.accountServiceManager.oauthService()
    private val accountService: AccountService get() = this.accountServiceManager.accountService()

    init {
        this.accountServiceManager = DefaultDinnerTonightAccountServiceManager(context = this.context, environment = this.environment)
    }

    fun authenticate(userName: String, password: String): Promise<Boolean> {
        this.accountServiceManager.setOAuthToken(null)
        return this.oauthService
                .authenticate(username = userName, password = password)
                .then{ token ->
                    // Always generate a new CrpSlt on new authentication
                    this.generateCryptoSalt()
                    token
                }
                .thenp{ token ->
                    this.didAuthenticate(token = token)
                }
    }

    fun reauthenticate(): Promise<Boolean> {
        val authToken = this.authTokenFromPreferences() ?: return Promise(value=false)
        this.accountServiceManager.setOAuthToken(authToken)
        return this.authenticate(authToken = authToken)
                .recoverp { error ->
                    this.unauthenticate().then { throw error }
                }
    }

    fun unauthenticate(): Promise<Boolean> {
        if (this.authToken == null) return Promise(value=false)
        val profileId = this.userProfile?.profileId ?: return Promise(value=false)
        return this.oauthService
                .unauthenticate(profileId = profileId)
                .recover{ _ -> true }
                .then(on= PromiseDispatch.MAIN) {
                    this.clearAuthSession()
                    this.delegate?.didUnauthenticate()
                    true
                }
    }

    fun isAuthenticated(): Boolean = (this.authToken != null && this.profile != null)

    fun createAccount(form: Registration): Promise<Boolean> {
        return this.accountService.createAccount(form = form)
                .thenp(on=null){ _ ->
                    this.authenticate(userName = form.email!!, password = form.password!!)
                }
    }

    fun reloadProfile(): Promise<Profile> {
        return this.accountService
                .getProfile(profileId = userProfile?.profileId!!)
                .thenp { profile ->
                    val preferredStore = profile.preferredStore
                    if (preferredStore == null || preferredStore == userProfile?.preferredStore) {
                        Promise(value=profile)
                    } else {
                        AppProxy.proxy.serviceManager().storeService()
                                .getStore(storeId = preferredStore)
                                .then{ store ->
                                    profile.preferredStoreDetails = store
                                    profile
                                }
                    }
                }
                .then{ profile ->
                    this.profile = profile
                    this.authToken?.profile = profile
                    this.userProfile!!
                }
    }

    fun resetPassword(email: String, phone: String, dob: Date): Promise<Boolean> =
            this.accountService.resetPassword(email = email, phone = phone, dob = DateFormatUtils.mmddyyyy().format(dob))

    fun resetPassword(email: String): Promise<Boolean> = this.accountService.resetPassword(email = email)

    fun changePassword(from: String, to: String): Promise<Boolean> {
        if (!this.isAuthenticated()) return Promise(error=Throwable("Cannot change password for unauthenticated user."))
        return this.accountService.changePassword(from = from, to = to, profileId = this.userProfile?.profileId ?: "")
    }

    fun updateProfile(form: UpdateProfile): Promise<Profile> {
        if (!this.isAuthenticated()) return Promise(error=Throwable("Cannot update profile for unauthenticated user."))
        return this.accountService.updateProfile(form = form)
                .thenp {
                    val currentPreferredStore = this.userProfile?.preferredStore
                    this.profile?.update(form)

                    val preferredStore = currentPreferredStore
                    val updatePreferredStore = form.preferredStore
                    if (preferredStore != null && updatePreferredStore != null && preferredStore != updatePreferredStore) {
                        AppProxy.proxy.serviceManager().storeService()
                                .getStore(storeId=updatePreferredStore)
                                .then {
                                    this.profile?.preferredStoreDetails = it
                                    this.profile!!
                                }
                    } else {
                        Promise(value=this.profile!!)
                    }
                }
                .then {
                    val authToken = this.authToken
                    authToken?.profile = it
                    if (authToken != null) {
                        this.saveAuthTokenToPreferences(authToken = authToken)
                    }
                    it
                }
    }

    //
    // Private Methods
    //

    private fun authenticate(authToken: OAuthToken): Promise<Boolean> {
        if (authToken.refreshToken == null) return Promise(IllegalArgumentException("Invalid oauth refresh token"))
        return this.oauthService
                .authenticate(refreshToken = authToken.refreshToken!!)
                .thenp { token ->
                    // Yeah, so this does happen on refresh token
                    if (token.profile == null) {
                        // Temporary set the oauth token for the getProfile() call
                        this.accountServiceManager.setOAuthToken(token)
                        this.accountService
                                .getProfile(profileId = authToken.profile?.profileId ?: "")
                                .then{ profile ->
                                    token.profile = profile
                                    token
                                }
                    } else {
                        Promise(value=token)
                    }
                }
                .thenp { token ->
                    this.didAuthenticate(token = token)
                }
                .recover { error ->
                    // Clear out the auth session if failed to authenticate
                    this.clearAuthSession()
                    throw error
                }
    }

    private fun didAuthenticate(token: OAuthToken): Promise<Boolean> {
        return Promise.void()
                .thenp {
                    this.saveAuthTokenToPreferences(token)
                    this.authToken = token
                    this.profile = token.profile

                    if (token.profile?.preferredStoreDetails == null) {
                        AppProxy.proxy.serviceManager().storeService()
                                .getStore(storeId = profile?.preferredStore ?: "")
                                .then { store ->
                                    this.profile?.preferredStoreDetails = store
                                    true
                                }
                    } else {
                        Promise(value=true)
                    }
                }
                .always {
                    this.accountServiceManager.setOAuthToken(token)
                    this.delegate?.didAuthenticate(token = token)
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

        this.authToken = null
        this.profile = null
        this.crpSlt = ""
        this.accountServiceManager.setOAuthToken(null)
    }

    private fun authTokenFromPreferences(): OAuthToken? {
        val preferences = AppProxy.proxy.preferences()
        if (this.crpSlt.isEmpty()) this.crpSlt = preferences.crpSlt()
        if (this.crpSlt.isEmpty()) return null

        val session = preferences.sessionInfo()
        return try {
            val key = CryptoService.getSecretKey(CrpAlias.AccountAlias.alias, this.crpSlt)
            val tokenStr = CryptoService.decrypt(key, session)
            JSONDecoder().decode<OAuthToken>(tokenStr.toByteArray(Charsets.UTF_8))
        } catch (ignored: Exception) {
            null
        }
    }

    private fun saveAuthTokenToPreferences(authToken: OAuthToken): Boolean {
        if (this.crpSlt.isEmpty()) return false

        try {
            val tokenData = JSONEncoder().encode(value = authToken)
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
