package com.heb.dtn.service.internal.dtn

import com.heb.dtn.foundation.extension.formUnion
import com.heb.dtn.foundation.promise.android.recover
import com.heb.dtn.foundation.promise.android.then
import com.heb.dtn.foundation.promise.android.thenp
import com.heb.dtn.foundation.service.HTTPService
import com.heb.dtn.foundation.service.JSONDecoder
import com.heb.dtn.foundation.service.decoder
import com.heb.dtn.foundation.service.post
import com.heb.dtn.service.api.AccountServiceError
import com.heb.dtn.service.api.AccountValidationFlags
import com.heb.dtn.service.api.SSOService
import com.heb.dtn.service.domain.account.*
import com.heb.dtn.service.domain.profile.Profile
import com.inmotionsoftware.promise.Promise

//
// Created by Maksim Orlovich on 10/11/17.
//

internal class GigyaConfig(
    val config: HTTPService.Config,
    val apiKey: String,
    val userKey: String,
    val secret: String
)

internal class GigyaSSOService(private val gigya: GigyaConfig): HTTPService(gigya.config), SSOService {
    private var session: Gigya.Accounts.SessionInfo? = null

    override fun authenticate(email: String, password: String): Promise<AuthResponse> {
        val body = Gigya.Accounts.LoginRequest(
                apiKey = this.gigya.apiKey
                , userKey = this.gigya.userKey
                , secret = this.gigya.secret
                , loginID = email
                , password = password
        )

        return this.send(route = "accounts.login"
                        , body = UploadBody.FormUrlEncoded(body.parameters())
                        , type = Gigya.Accounts.LoginResponse::class.java)
                .recover { error ->
                    if (error !is Gigya.Error) throw error
                    throw AccountServiceError.Unknown()
                }
                .then { resp ->
                    this.session = resp.sessionInfo

                    AuthResponse(
                        profile = Profile(
                            userId = resp.UID,
                            firstName = resp.profile?.firstName ?: "",
                            lastName = resp.profile?.lastName ?: "",
                            email = resp.profile?.email ?: ""
                        ),
                        uid = resp.UID,
                        uidSignature = resp.UIDSignature,
                        timestamp = resp.signatureTimestamp)
                }
    }

    override fun forgotPassword(email: String): Promise<Unit> {
        val body = Gigya.Accounts.ResetPasswordRequest(
                apiKey = this.gigya.apiKey
                , userKey = this.gigya.userKey
                , secret = this.gigya.secret
                , loginID = email
                , passwordResetToken = null
                , newPassword = null
        )

        return this.send(route = "accounts.resetPassword"
                        , body = UploadBody.FormUrlEncoded(body.parameters())
                        , type = Gigya.Accounts.ResetPasswordResponse::class.java)
                    .recover { error ->
                        if (error !is Gigya.Error) throw error
                        throw AccountServiceError.Unknown()
                    }
                    .asVoid()
    }

    override fun resetPassword(resetToken: String, password: String): Promise<Unit> {
        val body = Gigya.Accounts.ResetPasswordRequest(
                apiKey = this.gigya.apiKey
                , userKey = this.gigya.userKey
                , secret = this.gigya.secret
                , loginID = null
                , passwordResetToken = resetToken
                , newPassword = password
        )
        return this.send(route = "accounts.resetPassword"
                        , body = UploadBody.FormUrlEncoded(body.parameters())
                        , type = Gigya.Accounts.ResetPasswordResponse::class.java)
                    .recover { error ->
                        if (error !is Gigya.Error) throw error
                        throw AccountServiceError.Unknown()
                    }
                    .asVoid()
    }

    override fun registerAccount(email: String, password: String): Promise<Registration> {
        return this.initializeAccount()
                .thenp { this.registerAccount(regToken = it, email = email, password = password) }
    }

    //
    // Private Methods
    //

    fun initializeAccount(): Promise<RegistrationToken> {
        val body = Gigya.Accounts.InitRegistrationRequest(
                apiKey = this.gigya.apiKey
                , userKey = this.gigya.userKey
                , secret = this.gigya.secret
        )
        return this.send(route = "accounts.initRegistration"
                        , body = UploadBody.FormUrlEncoded(body.parameters())
                        , type = Gigya.Accounts.InitRegistrationResponse::class.java)
                    .recover { error ->
                        if (error !is Gigya.Error) throw error
                        throw AccountServiceError.Unknown()
                    }
                    .then { it.regToken }
    }

    fun registerAccount(regToken: RegistrationToken, email: String, password: String): Promise<Registration> {
        val body = Gigya.Accounts.RegisterRequest(
                apiKey = this.gigya.apiKey
                , userKey = this.gigya.userKey
                , secret = this.gigya.secret
                , email = email
                , password = password
                , regToken = regToken
        )
        return this.send(route = "accounts.register"
                        , body = UploadBody.FormUrlEncoded(body.parameters())
                        , type = Gigya.Accounts.RegisterResponse::class.java)
                    .recover { error ->
                        val gigyaError = error as? Gigya.Error ?: throw error
                        val validationFlags = AccountValidationFlags()

                        when (gigyaError.response.errorCode) {
                            Gigya.Error.Code.UniqueIdentifierExists -> validationFlags.formUnion(AccountValidationFlags.emailInUse)
                            Gigya.Error.Code.InvalidParameterValue -> {
                                if (gigyaError.response.errorDetails.endsWith(": Invalid email", ignoreCase = true)) {
                                    validationFlags.formUnion(AccountValidationFlags.email)
                                }
                            }
                            else -> { }
                        }

                        gigyaError.response.validationErrors?.forEach {
                            when (it.fieldName) {
                                "firstName" -> validationFlags.formUnion(AccountValidationFlags.firstName)
                                "lastName" -> validationFlags.formUnion(AccountValidationFlags.lastName)
                                "email" -> {
                                    when (it.errorCode) {
                                        Gigya.Error.Code.UniqueIdentifierExists -> validationFlags.formUnion(AccountValidationFlags.emailInUse)
                                        else -> validationFlags.formUnion(AccountValidationFlags.email)
                                    }
                                }
                                "password" -> validationFlags.formUnion(AccountValidationFlags.password)
                            }
                        }
                        throw if (validationFlags.isEmpty) AccountServiceError.Unknown() else AccountServiceError.Validation(flags = validationFlags)
                    }
                    .then { Registration(token = it.regToken, uid = it.UID, email = email, password = password) }
    }

    private fun <T, B:Any> HTTPService.send(route: String, body: HTTPService.UploadBody<B>, type: Class<T>): Promise<T>
        = this.post(route = route, body = body, type = type)
                .recover { error ->
                    val serviceError = error as? HTTPService.Error ?: throw error
                    when (serviceError) {
                        is HTTPService.Error.Response -> {
                            serviceError.body?.let {
                                val aDecoder = this.decoder(mimeType = serviceError.mimeType) ?: JSONDecoder()
                                val errorResp = aDecoder.decode(type = Gigya.ErrorResponse::class.java, value = it) ?: throw error
                                throw errorResp.throwable()
                            }
                        }
                    }
                    throw serviceError
                }

}
