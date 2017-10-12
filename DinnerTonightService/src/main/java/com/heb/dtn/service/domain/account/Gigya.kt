package com.heb.dtn.service.domain.account

import com.heb.dtn.foundation.service.HTTPService
import com.heb.dtn.service.api.RegistrationToken
import com.heb.dtn.service.api.UID
import java.util.Date

//
// Created by Maksim Orlovich on 10/11/17.
//

interface GigyaBaseRequest {
    var httpStatusCodes: Boolean
}

internal class Gigya private constructor () {

    class Params(
        var paramName: String,
        var warningCode: Int,
        var message: String
    )

    class ValidationErrors(
        var errorCode: Int,
        var message: String,
        var fieldName: String
    )
//MOTODO
//    class Error: Swift.Error, Decodable {
//        var statusCode: HTTPService.StatusCode
//        var errorCode: Code
//        var statusReason: String
//        var callId: String
//        var time: Date
//        var validationErrors: [ValidationErrors]?
//        var errorMessage: String
//        var errorDetails: String
//        var ignoredParams: [Params]?
//    }

    class Profile(
        var email: String,
        var firstName: String,
        var lastName: String,
        var age: String,
        var gender: String,
        var country: String
    )

    class ProviderUID(
        var providerUID: UID,
        var apiKey: String
    )

    class Identity(
//        var provider: String // The name of the connected provider for this identity, in lowercase letters ("facebook", "yahoo", etc.).
//        var providerUID: String // The user ID provided by the connected provider. For Facebook this is an application-scoped ID, in which case a user can have more than one providerUID. The 'mappedProviderUIDs' array contains all of the provider UIDs mapped to the user. Note: This field may be rather long for some providers; if you plan to store it in a DB the recommended field size is 300 characters.
//        var providerUIDSig: String // This field is available only if the signIDs field of the global configuration object was set to 'true'. The providerUIDSig field holds the HMAC-SHA1 hash of following string: "<timestamp>_<provider>_<providerUID>", where <timestamp>, <provider>, <providerUID> are substituted with the corresponding values.
//        var mappedProviderUIDs: [ProviderUID] // An array holding all the provider UIDs mapped to the user and the API keys associated with each provider UID.
//        var isLoginIdentity: Bool // Indicates whether this identity was the one that the user used in order to login.
//        var nickname: String // The person's nickname, this may be either the nickname provided by the connected provider or a concatenation of first and last name.
//        var allowsLogin: Bool // Indicates whether the user may use this identity for logging in to your site.
//        var isExpiredSession: Bool // Indicates whether the session has expired for this provider (or is otherwise inactive). This field is relevant and available only if when calling the getUserInfo method you have set the includeAllIdentities parameter to 'true'.
//        var lastLoginTime: Int64 // The time of the user's last login in Unix time format (i.e., the number of seconds since Jan. 1st 1970).
//        var photoURL: String // The URL of the person's full size photo.
//        var thumbnailURL: String // The URL of the person's thumbnail photo, if available.
        var firstName: String, // The person's first name.
        var lastName: String, // The person's last name.
//        var gender: String // The person's gender. The value may be 'm', 'f', or 'u' for male, female, or unspecified.
//        var age: Int // The person's age.
//        var birthDay: Int // The day of the month in which the person was born.
//        var birthMonth: Int // The month in which the person was born.
//        var birthYear: Int // The year in which the person was born.
        var email: String // The person's email.
//        var country: String // The person's country.
//        var state: String // The person's state.
//        var city: String // The person's city.
//        var zip: String // The person's zip code.
//        var profileURL: String // The URL of the person's profile.
//        var languages: String // A comma-separated list of languages that the person speaks.
//        var address: String // The person's address.
//        var phones: [String] // The person's phone numbers.
//        var education: array of objects // The person's education details.
//        var honors: String // A comma-separated list of the person's honors.
//        var publications: array of objects // The person's publications' details.
//        var patents: array of objects // The person's patents' details.
//        var certifications: array of objects // The person's certifications' details.
//        var professionalHeadline: String // The person's professional headline, often the job title at their company.
//        var bio: String // A description of the person's professional profile.
//        var industry: String // The industry in which the person's company operates.
//        var specialties: String // The person's specialties.
//        var work: array of objects // A collection of the person's work experience.
//        var skills: array of objects // A collection of the person's skills.
//        var religion: String // The person's religion.
//        var politicalView: String // The person's political views.
//        var interestedIn: String // The gender in which the person is interested.
//        var relationshipStatus: String // The relationship status of the person.
//        var hometown: String // The person's hometown.
//        var favorites: JSON object // The person's favorite things, including favorite books, movies, etc.
//        var likes: array of objects // Retrieves up to 500 of the person's Facebook likes.
//        var followersCount: Int // The number of other users following this user, when applicable (Twitter).
//        var followingCount: Int // The number of users this user is following, when applicable (Twitter).
//        var username: String // The person's Facebook user name.
//        var locale: String // The person's locale.
//        var verified: Bool // Indicates whether the person verified his email with Facebook.
//        var timezone: String // The person's timezone.
//        var missingPermissions: String // A comma-separated string that lists the differences between the general capabilities of Facebook and the requested capabilities. The requested capabilities are the ones checked in the console permissions, e.g., "missingPermissions": "Actions, Status, Photos".
//        var samlData: String
    )

    class Ids private constructor() {

        class GetAccountInfoRequest(
            override var httpStatusCodes: Boolean = true,
            var UID: UID,
            var regToken: RegistrationToken
        ) : GigyaBaseRequest

        class GetAccountInfoResponse(
            var UID: UID,
            var UIDSignature: String,
            var signatureTimestamp: String,
            var loginProvider: String,
            var isRegistered: Boolean,
            var isActive: Boolean,
            var isVerified: Boolean,
            var socialProviders: String,
            var profile: Profile,
//          var data: [String:Decodable]
            var identities: Array<Identity>,
            var created: Date,
            var createdTimestamp: Long,
            var lastLogin: Date,
            var lastLoginTimestamp: Long,
            var lastUpdated: Date,
            var lastUpdatedTimestamp: Long,
            var oldestDataUpdated: Date,
            var oldestDataUpdatedTimestamp: Long
        )
    }

    class Accounts private constructor() {

        class InitRegistrationRequest(
            override var httpStatusCodes: Boolean = true,
            var apiKey: String,
            var userKey: String,
            var secret: String
        ) : GigyaBaseRequest


        class InitRegistrationResponse(
            var regToken: String
        )

        class RegisterRequest(
            override var httpStatusCodes: Boolean = true,
            var apiKey: String,
            var userKey: String,
            var secret: String,
            var email: String,
            var password: String,
            var regToken: String
        ) : GigyaBaseRequest

        class RegisterResponse(
            var regToken: String,
            var sessionInfo: SessionInfo?,
            var UID: String,
            var UIDSignature: String,
//            var UIDSignature: ByteArray,
            var signatureTimestamp: String,
            var loginProvider: String,
            var isRegistered: Boolean,
            var registeredTimestamp: Long?,
//            var registered: Date?,
            var isActive: Boolean,
            var isVerified: Boolean,
            var verifiedTimestamp: Long?,
//            var verified: Date?,
            var socialProviders: String,
//            var profile: Profile?,
//            var created: Date,
            var createdTimestamp: Long,
//            var lastLogin: Date,
            var lastLoginTimestamp: Long,
//            var lastUpdated: Date,
            var lastUpdatedTimestamp: Long,
//            var oldestDataUpdated: Date,
            var oldestDataUpdatedTimestamp: Long
        ) {
            class SessionInfo(
                var cookieName: String,
                var cookieValue: String)
        }
    }
}

//MOTODO
//extension Gigya.Error {
//        enum Code: Int, Decodable {