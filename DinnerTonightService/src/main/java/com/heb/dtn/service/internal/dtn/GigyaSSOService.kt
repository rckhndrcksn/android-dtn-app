package com.heb.dtn.service.internal.dtn

import com.heb.dtn.foundation.promise.android.then
import com.heb.dtn.foundation.service.Decoder
import com.heb.dtn.foundation.service.HTTPService
import com.heb.dtn.foundation.service.JSONDecoder
import com.heb.dtn.foundation.service.post
import com.heb.dtn.service.api.*
import com.heb.dtn.service.domain.account.Gigya
import com.heb.dtn.service.domain.account.GigyaBaseRequest
import com.heb.dtn.service.domain.account.Registration
import com.inmotionsoftware.promise.Promise

//
// Created by Maksim Orlovich on 10/11/17.
//

// RESP:
//{
//  "regToken": "RT1_dBFII5RbVxUc8nBdc3bMDT7hmmrIvgen1wCG_dxqadJhAAWkNZSIhV-1DGKZvwZ0-DQUg5JS8Y61ukrjwOp8p81S9pP6R_BhovjemyHtbA0dAsx-PMuL2zIosIac-rUvj3lTh1WL6rg0IY1bFO3pdrgQvxN_Pxa0r6kxTb5QiZNn7ojd-SXYnLUgrW6iw-OIzAzp8hZBMN_hpwEauylxTfJM_9nGpnT_QxLnoKTAEvzIhK-yFU9PJlT_RA7tJzjahTxqRxU3Qd3KsYtAu-K5wRoRNGpVMTWQbKoe6lIeSKnF6lFNeozDiuv1Vat6c9qE8RVOhp8leM9NAWG_3937L2MchFEFbBNqsgKk0QZ8Ow_SG03SS2RhQpq8KhG28A-47i1PXY041LHP_J8NwtUI8u0_ObVDsIInv3h33aKPQN1BxY4DxICSXtS8iuL7dsgG2T0KuegagIoYiNhLbnSeowkRRkZ-wiiKJOTupKGw7w6bmjqwBhqGWo9cudKLMFT22lsZ3oqah3SXizOi8NKP6g..",
//  "statusCode": 200,
//  "errorCode": 0,
//  "statusReason": "OK",
//  "callId": "5d18ece8a76f422e917bbbf094c5cf11",
//  "time": "2015-03-22T11:42:25.943Z"
//}

// https://accounts.us1.gigya.com/accounts.register?apiKey=${apiKey}&secret=${secret}&email=${email}&password=${pass}&firstName=${firstName}&regToken=${regToken}
// RESP:
//{
//  "sessionInfo": {
//    "cookieName": "gac_2_ddxTpQZ_zGiuCsCePVKC6bZcBp_qD-pjql",
//    "cookieValue": "VC1_739B3B4AD534B6F62AHNld3Knl98Q_vGL5_SxwA=="
//  },
//  "UID": "e862a450214c46b3973ff3c8368d1c7e",
//  "UIDSignature": "iwPwRr3oDmbb8hhTeoO5JHTrc2Y=",
//  "signatureTimestamp": "1344415327",
//  "loginProvider": "site",
//  "isRegistered": true,
//  "registeredTimestamp": 1344415327000,
//  "registered": "2012-08-08T08:42:07Z",
//  "isActive": true,
//  "isVerified": true,
//  "verifiedTimestamp": 1344413279133,
//  "verified": "2012-08-08T08:07:59.133Z",
//  "socialProviders": "site",
//  "profile": {
//    "email": "Joe@hotmail.com",
//    "firstName": "Joe",
//    "lastName": "Smith",
//    "age" : "31",
//    "gender" : "m",
//    "country" : "US"
//  },
//  "created": "2012-08-08T08:07:59.128Z",
//  "createdTimestamp": 1344413279128,
//  "lastLogin": "2012-08-08T08:42:07Z",
//  "lastLoginTimestamp": 1344415327000,
//  "lastUpdated": "2012-08-08T08:07:59.133Z",
//  "lastUpdatedTimestamp": 1344413279133,
//  "oldestDataUpdated": "2012-08-08T08:07:59.133Z",
//  "oldestDataUpdatedTimestamp": 1344413279133,
//  "statusCode": 200,
//  "errorCode": 0,
//  "statusReason": "OK",
//  "callId": "8fb3eaf37a424cae8c3e6fe3f53cc177",
//  "time": "2015-03-22T11:42:25.943Z"
//}


//{
//  "validationErrors": [
//    {
//      "errorCode": 400003,
//      "message": "email already exists",
//      "fieldName": "email"
//    }
//  ],
//  "errorMessage": "Validation error",
//  "errorDetails": "Validation failed",
//  "statusCode": 400,
//  "errorCode": 400009,
//  "statusReason": "Bad Request",
//  "callId": "96b1a3687f444412a81b9ebebdefe795",
//  "time": "2017-10-06T22:01:33.416Z",
//  "ignoredParams": [
//    {
//      "paramName": "firstName",
//      "warningCode": 403007,
//      "message": "This parameter was not recognized as valid for this API method with your security credentials nor was it recognized as a standard Gigya control parameter."
//    }
//  ]
//}

//{
//  "UID": "ff3c8368d1c7e",
//  "UIDSignature": "5LA3g3qTmf0GPE=",
//  "signatureTimestamp": "1344413375",
//  "loginProvider": "facebook",
//  "isRegistered": true,
//  "isActive": true,
//  "isVerified": true,
//  "socialProviders": "site,facebook",
//  "profile": {
//    "email": "Joe@hotmail.com",
//    "firstName": "Joe",
//    "lastName": "Smith",
//    "age" : "31",
//    "gender" : "m",
//    "country" : "US"
//  },
//  "data": {
//    "newsletter": "true",
//    "forums" : "news,entertainment"
//  },
//  "identities": [
//    {
//      "provider": "site",
//      "providerUID": "d1c7e",
//      "isLoginIdentity": false,
//      "allowsLogin": false,
//      "isExpiredSession": false,
//      "lastUpdated": "2012-08-08T08:07:59.133Z",
//      "lastUpdatedTimestamp": 1344413279133,
//      "oldestDataUpdated": "2012-08-08T08:07:59.133Z",
//      "oldestDataUpdatedTimestamp": 1344413279133
//    },
//    {
//      "provider": "facebook",
//      "providerUID": "k97y",
//      "isLoginIdentity": true,
//      "gender": "m",
//      "email": "Joe@hotmail.com",
//      "firstName": "Joe",
//      "lastName": "Smith",
//      "age" : "31",
//      "country" : "US"
//      "allowsLogin": true,
//      "isExpiredSession": false,
//      "lastUpdated": "2012-09-08T08:07:59.133Z",
//      "lastUpdatedTimestamp": 1344413274523,
//      "oldestDataUpdated": "2012-09-08T08:07:59.133Z",
//      "oldestDataUpdatedTimestamp": 1344413276453
//    }
//  ],
//  "created": "2012-08-08T08:07:59.128Z",
//  "createdTimestamp": 1344413279128,
//  "lastLogin": "2012-08-08T08:09:17Z",
//  "lastLoginTimestamp": 1344413357000,
//  "lastUpdated": "2012-09-08T08:07:59.133Z",
//  "lastUpdatedTimestamp": 1344413279133,
//  "oldestDataUpdated": "2012-08-08T08:07:59.133Z",
//  "oldestDataUpdatedTimestamp": 1344413279133,
//  "statusCode": 200,
//  "errorCode": 0,
//  "statusReason": "OK",
//  "callId": "9999996",
//  "time": "2015-03-22T11:42:25.943Z"

class GigyaConfig(
    val config: HTTPService.Config,
    val apiKey: String,
    val userKey: String,
    val secret: String
)

class GigyaSSOService(private val gigya: GigyaConfig): HTTPService(gigya.config), SSOService {

    public fun getAccount(uid: UID, token: RegistrationToken): Promise<Unit> {
        val body = mapOf(
            Pair("httpStatusCodes", "true"),
                Pair("UID", uid),
                Pair("regToken", token)
        )
//        val body = Gigya.Ids.GetAccountInfoRequest(
//            httpStatusCodes = true,
//            UID = uid,
//            regToken = token
//        )

        return this.post(route = "ids.getAccountInfo", body = UploadBody.FormUrlEncoded(body), type = Gigya.Ids.GetAccountInfoResponse::class.java).asVoid()
    }

    override fun initializeAccount(): Promise<RegistrationToken> {
        val body = mapOf(
            Pair("httpStatusCodes", "true"),
            Pair("apiKey", this.gigya.apiKey),
            Pair("userKey", this.gigya.userKey),
            Pair("secret", this.gigya.secret)
        )

        return this.post(route = "accounts.initRegistration", body = UploadBody.FormUrlEncoded(body), type = Gigya.Accounts.InitRegistrationResponse::class.java)
            .then {
                it.regToken
            }
    }

    override fun registerAccount(regToken: RegistrationToken, email: String, password: String): Promise<Registration> {
        val body = mapOf(
            Pair("httpStatusCodes", "true"),
            Pair("targetEnv", "mobile"),
            Pair("apiKey", this.gigya.apiKey),
            Pair("userKey", this.gigya.userKey),
            Pair("secret", this.gigya.secret),
            Pair("email", email),
            Pair("password", password),
            Pair("regToken", regToken)
        )

        return this.post(route = "accounts.register", body = UploadBody.FormUrlEncoded(body), type = Gigya.Accounts.RegisterResponse::class.java)
                    .then {
                        Registration(token = it.regToken, uid = it.UID, email = email, password = password)
                    }
    }

}
