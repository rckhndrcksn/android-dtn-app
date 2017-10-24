package com.heb.dtn.service.domain.account

import com.heb.dtn.foundation.service.HTTPService
import com.heb.dtn.foundation.service.Parameters
import com.heb.dtn.service.domain.GigyaErrorCode
import com.heb.dtn.service.domain.HTTPServiceStatusCode
import com.heb.dtn.service.domain.ParametersAdapter
import java.util.*

//
// Created by Maksim Orlovich on 10/11/17.
//

typealias RegistrationToken = String
typealias UID = String

internal class Gigya private constructor () {

    class Params(
        var paramName: String,
        var warningCode: Int,
        var message: String
    )

    class ValidationErrors(
        @GigyaErrorCode
        var errorCode: Error.Code,
        var message: String,
        var fieldName: String
    )

    class ErrorResponse(
        @HTTPServiceStatusCode
        var statusCode: HTTPService.StatusCode,

        @GigyaErrorCode
        var errorCode: Error.Code,

        var statusReason: String,
        var callId: String,
        var time: Date,
        var validationErrors: List<ValidationErrors>?,
        var errorMessage: String,
        var errorDetails: String,
        var ignoredParams: List<Params>?
    ) {
        fun throwable(): Error = Error(response = this)
    }

    class Error(val response: ErrorResponse): Throwable() {

         enum class Code(val value: Int) {
             Unknown(-1)
             , Success (0)
             , DataPending (100001) // Data is still being processed. Please query again for the response.
             , OperationCanceled (200001) // User canceled during the login process.
             , OKWithErrors (200008) // For reports purposes, when OK is returned but there were acceptable errors in the process.
             , AccountsLinked (200009) // The accounts have been linked successfully.
             , OKWithErrorLoginIdentifierExists (200010) // When a new account is created and the login identifier already exists, the server handles the conflict according to the conflictHandling parameter. If saveProfileAndFail is passed, the profile data is saved, a registration token is returned for account linking, and this error is returned.
             , AccountPendingRegistration (206001) // A method has been called that performs social login, but the registration process has not been finalized, or a required field is missing from the user profile or data. See Accounts API Error Codes and Messages for more information.
             , AccountPendingVerification (206002) // An account has already been verified and a user tries to log in with a loginID (usually an email address) whose connection to the user has not been verified. See Accounts API Error Codes and Messages for more information.
             , AccountMissingLoginID (206003) // The registration policy requires a loginID when a user uses Social Login to register to the site, but there are no login identifiers or a password associated with the account. See Accounts API Error Codes and Messages for more information.
             , IdentitiesWereConflicted (206004) // An identity conflict has occurred during account import. This means that a providerUID being imported matches one that is already in the system.
             , PendingAutologinFinalization (206005) // When auto-login from email verification link policy is activated, this response code is passed as the user is redirected to the nextURL specified in the policy. It is not indicative of an error.
             , InvalidDataCenter (301001) // The API key is served by another data center. The error occurs when an API request is received at the wrong data center.
             , InvalidRequestFormat (400001) // This error may be caused by various faults in the request. For example: * wrong authentication header * non-secure request that should be secured.
             , MissingRequiredParameter (400002) // The method requires some parameters. One of the required parameters was not set in this method call. The error message will include the name of the missing parameter.
             , UniqueIdentifierExists (400003) // A user tries to register or set the account information with an email or username that already exists in the accounts database. See Accounts API Error Codes and Messages for more information.  Some possible response messages are: * If a chosen Username already exists the returned message is Username already exists.* If a chosen Email already exists the returned message is Email already exists.
             , InvalidParameterFormat (400004) // One of the parameters of this request has been set with a value which is not in the expected format.
             , InvalidParameterValue (400006) // One of the parameters of this request has been set with a value which is not within the parameter's defined value bounds. Please refer to the method's parameter table, and check the definition of valid values per parameter. The error message will include the name of the specific parameter.
             , DuplicateValue (400007) // Internal error.
             , InvalidAuthenticationHeader (400008) // An OAuth2 error. See OAuth2 Error Response for more information.
             , Validation (400009) // In accounts.register, whenever there is a validation error. Some possible response messages are:* If input Password Doesn't meet policy requirements (or is larger than 30 characters) the returned message is "Password does not meet complexity requirements". * If input Password Confirmation does not match Password field the returned message is Passwords do not match.* If any Invalid or unsupported input (all fields) is detected the returned message is Invalid %fieldname.
             , InvalidRedirectURI (400011) // An OAuth2 error. See OAuth2 Error Response for more information.
             , InvalidResponseType (400012) // An OAuth2 error. See OAuth2 Error Response for more information.
             , UnsupportedGrantType (400013) // An OAuth2 error. See OAuth2 Error Response for more information.
             , InvalidGrant (400014) // An OAuth2 error. See OAuth2 Error Response for more information.
             , CodeExpired (400015) // An OAuth2 error. See OAuth2 Error Response for more information.
             , SchemaValidationFailed (400020) // There was an attempt to write to fields from the client side. By default, only signed requests coming from the server are allowed to write into the data fields.
             , CAPTCHAVerificationFailed (400021) // The registration policy requires the user to pass a CAPTCHA test in order to register and the CAPTCHA verification has failed. See Accounts API Error Codes and Messages for more information.
             , UniqueIndexValidation  (400022) // Used mostly for DS, where custom unique indexes are supported.
             , InvalidTypeValidation  (400023) // When the internal type (string, int, date, etc) does not match the type of the provided value.
             , DynamicFieldsValidation  (400024) // A validation error is returned whenever there is a data validation error regarding one of the following required fields: username, password, secretQuestion, secretAnswer, email.
             , WriteAccessValidation (400025) //  A write access error regarding one of the following required fields: * username * password * secretQuestion * secretAnswer * email
             , InvalidFormatValidation  (400026) // Invalid regex format.
             , RequiredValueValidation (400027) // A required value is missing or has an error in one of the following required fields: * username * password * secretQuestion * secretAnswer * email Some possible response messages are: * If CAPTCHA input is blank or incorrect the returned message is "The characters you entered didn't match the word verification. Please try again". * If a required field (all fields) is not complete the returned message is "This field is required".
             , EmailNotVerified (400028) // The email address provided has not been verified.
             , SchemaConflict (400029) // An internal error was encountered while indexing the object.
             , OperationNotAllowed (400030) // This error is returned if a user logs in with a SAML provider, and multiple identities are not allowed, and a call to socialize.addConnection or to socialize.removeConnection is attempted.
             , RegexTooComplex (400031) // This error is returned if your implementation includes a custom regex for validating the email format of the profile.email field in registration screens (defined using accounts.setSchema), and the regex is so complex that it impedes performance.
             , SecurityVerificationFailed  (400050) // With accounts.resetPassword when the provided credentials could not be verified.
             , InvalidApiKeyParameter (400093) // The provided API key is invalid.
             , NotSupported (400096) // The function is not supported by any of the currently connected providers.
             , BrowserInsecure (400097) // The user is attempting to access Gigya services from an insecure/unsupported browser. User should switch browsers.
             , NoProviders (400100) // With accounts.tfa.importTFA or accounts.tfa.resetTFA when no such TFA provider exists.
             , InvalidContainerID (400103) // The containerID specified does not exist.
             , NotConnected (400106) // User is not connected to the required network or to any network.
             , InvalidSiteDomain (400120) // The current domain does not match the domain configured for the api key.
             , ProviderConfigurationError (400122) // An error originated from a provider.
             , LimitReached (400124) // Refers generally to any reached limits, either in Loyalty or in Comments. In Loyalty, when a user performed more actions than the allowed daily cap (maximum actions per 24 hrs), or when a user performed actions more frequently than the allowed frequency cap (minimum interval between consecutive actions). So the error can be DailyCap exceeded or FreqCap exceeded. In commenting, the error is returned when a user reaches the daily limit of new comments threads per stream.
             , FrequencyLimitReached (400125) // A comments spam cap was reached.
             , InvalidAction (400126) // In Gamification when the action is invalid.
             , InsufficientPointsToRedeem (400127) // When the gamification method redeemPoints is called, and the user does not have enough points, the operation fails and this error occurs.
             , InvalidPolicyConfiguration (401000) // If Protect Against Account Harvesting policy is enabled and neither Email Validation nor CAPTCHA Validation policies are enabled.
             , MediaItemsNotSupported (401001) // When media items are not allowed for this category.
             , SuspectedSpam (401010) // If someone is trying to use Gigya to send an email with a URL that does not match any of the client's domains.
             , LoginFailedCaptchaRequired (401020) // If accounts.login is attempted and the CAPTCHA threshold has been reached. The CAPTCHA threshold is set in the site Policies (security.captcha.failedLoginThreshold policy).
             , LoginFailedWrongCaptcha (401021) // If accounts.login is attempted and the CAPTCHA threshold has been reached and the provided CAPTCHA text is wrong. The CAPTCHA threshold is set in the site Policies (security.captcha.failedLoginThreshold policy).
             , OldPasswordUsed (401030) // The password provided is not the correct current password, however, it is a password previously associated with the account. This may appear in the following cases:* When accounts.login is attempted with a password that doesn't match the current password, but does match the previous one. In this case, the server will return this error with a message saying that "the password was modified on" the date when the current password was set. * When accounts.resetPassword is attempted with a password that has previously been used with the account. In this case, the server will return this error with a message stating "invalid password: the provided password was already in use by this account".
             , Forbidden (403000) // You do not have permission to invoke the method.
             , RequestHasExpired (403002) // The timestamp or expiration of the token used exceeded the allowed time window. * The most common cause for this error is when your server's clock is not accurately set. This causes a gap between your time and Gigya's time. Even a gap of two minutes is enough to create this error.* Please refer to Signing requests for more details.
             , InvalidRequestSignature (403003) // The request is not signed with a valid signature. Please refer to Signing requests for more details.
             , DuplicateNonce (403004) // The value of the nonce parameter that was passed with this request is not unique. Gigya requires that in each REST API call the nonce string will be unique. If Gigya receives two API calls with the same nonce, the second API call is rejected. Please refer to Signing requests for more details.
             , UnauthorizedUser (403005) // The user ID that is passed is not valid for this site.
             , SecretSentOverHttp (403006) // When sending the secret key in REST it has to be over HTTPS.
             , PermissionDenied (403007) // Returned when a user lacks the necessary permissions to perform the requested action, or when the user's credentials are not configured properly.
             , InvalidOpenIDUrl (403008) // Cannot find an openId endpoint on the url or cannot find the username given for the openId login.
             , ProviderSessionExpired (403009) // The user session for this provider has expired.
             , InvalidSecret (403010) // The request has an invalid secret key.
             , SessionHasExpired (403011) // The session for this user has expired.
             , NoValidSession (403012) // Requested user has no valid session.
             , MissingRequestReferrer (403015) // We can't validate the request because the referrer header is missing.
             , UnexpectedProviderUser (403017) // The user currently logged in to the requested provider is not the same as the one logged in to the site.
             , PermissionNotRequested (403022) // This operation needs a user permission and it was not requested. You may use the method socialize.requestPermissions to request the user permission. After gaining user permission you may retry to execute this operation.
             , NoUserPermission (403023) // This operation needs a user permission and the user did not grant your application with the necessary permission.
             , ProviderLimitReached (403024) // Limit reached: Status is a duplicate. This error occurs when a user shares content multiple times, and is returned with the provider name, e.g., "provider" : "twitter".
             , InvalidToken (403025) // Invalid OAuth2 token. Read more in Using Gigya's REST API in compliance with OAuth 2.0.
             , UnauthorizedAccessError (403026) // Returned from the accounts.isAvailableLoginID method, when Protect Against Account Harvesting policy is enabled.
             , ApprovedByModerator (403031) // Can't flag comment, it was already approved by a moderator.
             , NoUserCookie (403035) // The request is missing user credentials.
             , UnauthorizedPartner (403036) // The relevant Gigya product is not enabled for this partner.
             , PostDenied (403037) // Comments - Post denied when the user tried to review twice.
             , NoLoginTicket (403040) // No login ticket in callback URL.
             , AccountDisabled (403041) // A user has tried to log into an inactive account. See Accounts API Error Codes and Messages for more information.
             , InvalidLoginID (403042) // A user passes an incorrect password or a login ID that doesn't exist in our accounts database. See Accounts API Error Codes and Messages for more information.
             , LoginIdentifierExists (403043) // The username/email address provided by the user exists in the database but is associated with a different user. See Accounts API Error Codes and Messages for more information.
             , UnderageUser (403044) // A user under the age of 13 has tried to log in. For COPPA compliance (Children's Online Privacy Protection Act). Please refer to the Age Limit section in the Policies guide.
             , InvalidSiteConfigurationError (403045) // If Registration-as-a-Service (RaaS) is enabled for your site, but the storage (DS) size has not been configured.
             , LoginIDDoesNotExist (403047) // There is no user with that username or email. In the "Forgot Password" screen of a Gigya Screen-Set, this error is returned if a user fills in an email of a user that doesn't exist.
             , APIRateLimitExceeded (403048) // The daily API call limit has been reached.
             , PendingPasswordChange (403100) // When accounts.login is attempted and the password change interval has passed since the last password change. The interval is set in the site Policies (security.passwordChangeInterval policy).
             , AccountPendingTFAVerification (403101) // When accounts.login, accounts.socialLogin, accounts.finalizeRegistration, socialize.notifyLogin, or socialize.login is called and the RBA policy requires two-factor authentication, and the device is not already in the verified device list for the account. The first time the method is called, the device needs to be registered, and for the following calls, the device needs to be verified.
             , AccountPendingTFARegistration ( 403102) // When accounts.login, accounts.socialLogin, accounts.finalizeRegistration, socialize.notifyLogin, or socialize.login is called and the RBA policy requires two-factor authentication, and the device is not already in the verified device list for the account. The first time the method is called, the device needs to be registered, and for the following calls, the device needs to be verified.
             , AccountPendingRecentLogin  (403110) // When there is an attempt to deactivate a TFA provider for a user (with accounts.tfa.deactivateProvider) or to register a user (with accounts.tfa.initTFA) and the user did not log in through the device in the last few minutes.
             , AccountTemporarilyLockedOut (403120) // When accounts.login is attempted and the account is locked out or the originating IP is locked out. This occurs after a set number of failed login attempts. The number is set in the site Policies - security.accountLockout.failedLoginThreshold policy and security.ipLockout.hourlyFailedLoginThreshold policy.
             , RedundantOperation (403200) // When the client performs an operation that is redundant.
             , InvalidApplicationID (403201) // When the provided app ID is different from the one configured for the site.
             , NotFound (404000) //When returned from a comments API: category not found. When returned from an accounts API: email verification failed.
             , FriendNotFound (404001) // The friend user ID provided is not a friend for the current user.
             , CategoryNotFound (404002) // Comments - Category not found.
             , UIDNotFound (404003) // Caused by an invalid UID, or a UID not applicable to the current API key.
             , InvalidURL (404004) // An embed.ly 404 error message returned when the URL is invalid.
             , InvalidAPIMethod (405001) // Internal for Gigya JavaScript Web SDK.
             , IdentityExists (409001) // When attempting to connect to a provider that is already connected or to link to an already linked account.
             , MissingUserPhoto (409010) // When calling accounts.getProfilePhoto, accounts.publishProfilePhoto or accounts.uploadProfilePhoto. The user photo requested does not exist or the photo provided is not valid.
             , CounterNotRegistered (409011) // There was an attempt to set or retrieve information in a counter that the system cannot find. See accounts.incrementCounters.
             , InvalidGmidTicket (409012) // See 3rd Party Cookies for information about using gmid tickets.
             , SAMLMappedAttributeNotFound (409013) // When a mapped attribute value for the providerUID cannot be retrieved.
             , SAMLCertificateNotFound (409014) // When the SAML certificate cannot be retrieved.
             , NoProviderSession (409031) // When a request to a social provider is pending, but a required authToken is missing.
             , CertInvalidCName (409040) // A Cname failed to validate. Possible causes are apiPrefix is empty or the domain name was not found or there was a name mismatch.
             , Gone (410000) // Resource is no longer available.
             , RequestEntityTooLarge (413001) // Comments plugin received a request that was too large.
             , CommentTextTooLarge (413002) // Comments plugin received a comment with too much text.
             , ObjectTooLarge (413003) // The data store object size is too large, it is limited to 512KB.
             , ProfilePhotoTooLarge (413004) // The profile photo exceeded file-size limits, or uses a non-supported format.
             , GeneralSecurityWarning  (500000) // General security warning.
             , GeneralServerError (500001) // General server error.
             , ServerLoginError (500002) // General error during the login process.
             , DefaultApplicationConfiguration (500003) // For multiple Data Centers (DCs) when no default application can be found.
             , SessionMigrationError (500014) // Error while migrating old Facebook session to new Graph API Facebook session.
             , ProviderError (500023) // General error from the provider.
             , NetworkError (500026) // Various network errors, e.g., when a JSONP request fails.
             , DatabaseError (500028) // General database error.
             , NoProviderApplication (500031) // There is no definition of provider application for this site. Please refer to Opening External Applications to learn how to define a provider application.
             , InvalidEnvironmentConfig (500033) // When there is no target environment in the config file.
             , ErrorDuringBackendOperation (500034) // Internal error.
             , Timeout (504001) // Client-side error.
             , RequestTimeout (504002) // A timeout that was defined in the request is reached.
             ;

             companion object {
                 fun withValue(value: Int): Code =
                     when(value) {
                         0 -> Success
                         100001 -> DataPending
                         200001 -> OperationCanceled
                         200008 -> OKWithErrors
                         200009 -> AccountsLinked
                         200010 -> OKWithErrorLoginIdentifierExists
                         206001 -> AccountPendingRegistration
                         206002 -> AccountPendingVerification
                         206003 -> AccountMissingLoginID
                         206004 -> IdentitiesWereConflicted
                         206005 -> PendingAutologinFinalization
                         301001 -> InvalidDataCenter
                         400001 -> InvalidRequestFormat
                         400002 -> MissingRequiredParameter
                         400003 -> UniqueIdentifierExists
                         400004 -> InvalidParameterFormat
                         400006 -> InvalidParameterValue
                         400007 -> DuplicateValue
                         400008 -> InvalidAuthenticationHeader
                         400009 -> Validation
                         400011 -> InvalidRedirectURI
                         400012 -> InvalidResponseType
                         400013 -> UnsupportedGrantType
                         400014 -> InvalidGrant
                         400015 -> CodeExpired
                         400020 -> SchemaValidationFailed
                         400021 -> CAPTCHAVerificationFailed
                         400022 -> UniqueIndexValidation
                         400023 -> InvalidTypeValidation
                         400024 -> DynamicFieldsValidation
                         400025 -> WriteAccessValidation
                         400026 -> InvalidFormatValidation
                         400027 -> RequiredValueValidation
                         400028 -> EmailNotVerified
                         400029 -> SchemaConflict
                         400030 -> OperationNotAllowed
                         400031 -> RegexTooComplex
                         400050 -> SecurityVerificationFailed
                         400093 -> InvalidApiKeyParameter
                         400096 -> NotSupported
                         400097 -> BrowserInsecure
                         400100 -> NoProviders
                         400103 -> InvalidContainerID
                         400106 -> NotConnected
                         400120 -> InvalidSiteDomain
                         400122 -> ProviderConfigurationError
                         400124 -> LimitReached
                         400125 -> FrequencyLimitReached
                         400126 -> InvalidAction
                         401000 -> InvalidPolicyConfiguration
                         401001 -> MediaItemsNotSupported
                         401010 -> SuspectedSpam
                         401020 -> LoginFailedCaptchaRequired
                         401021 -> LoginFailedWrongCaptcha
                         401030 -> OldPasswordUsed
                         403000 -> Forbidden
                         403002 -> RequestHasExpired
                         403003 -> InvalidRequestSignature
                         403004 -> DuplicateNonce
                         403005 -> UnauthorizedUser
                         403006 -> SecretSentOverHttp
                         403007 -> PermissionDenied
                         403008 -> InvalidOpenIDUrl
                         403009 -> ProviderSessionExpired
                         403010 -> InvalidSecret
                         403011 -> SessionHasExpired
                         403012 -> NoValidSession
                         403015 -> MissingRequestReferrer
                         403017 -> UnexpectedProviderUser
                         403022 -> PermissionNotRequested
                         403023 -> NoUserPermission
                         403024 -> ProviderLimitReached
                         403025 -> InvalidToken
                         403026 -> UnauthorizedAccessError
                         403031 -> ApprovedByModerator
                         403035 -> NoUserCookie
                         403036 -> UnauthorizedPartner
                         403037 -> PostDenied
                         403040 -> NoLoginTicket
                         403041 -> AccountDisabled
                         403042 -> InvalidLoginID
                         403043 -> LoginIdentifierExists
                         403044 -> UnderageUser
                         403045 -> InvalidSiteConfigurationError
                         403047 -> LoginIDDoesNotExist
                         403048 -> APIRateLimitExceeded
                         403100 -> PendingPasswordChange
                         403101 -> AccountPendingTFAVerification
                         403102 -> AccountPendingTFARegistration
                         403110 -> AccountPendingRecentLogin
                         403120 -> AccountTemporarilyLockedOut
                         403200 -> RedundantOperation
                         403201 -> InvalidApplicationID
                         404000 -> NotFound
                         404001 -> FriendNotFound
                         404002 -> CategoryNotFound
                         404003 -> UIDNotFound
                         404004 -> InvalidURL
                         405001 -> InvalidAPIMethod
                         409001 -> IdentityExists
                         409010 -> MissingUserPhoto
                         409011 -> CounterNotRegistered
                         409012 -> InvalidGmidTicket
                         409013 -> SAMLMappedAttributeNotFound
                         409014 -> SAMLCertificateNotFound
                         409031 -> NoProviderSession
                         409040 -> CertInvalidCName
                         410000 -> Gone
                         413001 -> RequestEntityTooLarge
                         413002 -> CommentTextTooLarge
                         413003 -> ObjectTooLarge
                         413004 -> ProfilePhotoTooLarge
                         500000 -> GeneralSecurityWarning
                         500001 -> GeneralServerError
                         500002 -> ServerLoginError
                         500003 -> DefaultApplicationConfiguration
                         500014 -> SessionMigrationError
                         500023 -> ProviderError
                         500026 -> NetworkError
                         500028 -> DatabaseError
                         500031 -> NoProviderApplication
                         500033 -> InvalidEnvironmentConfig
                         500034 -> ErrorDuringBackendOperation
                         504001 -> Timeout
                         504002 -> RequestTimeout
                         else -> Unknown
                     }
             }
        }
    }

    class Profile(
        var email: String,
        var firstName: String?,
        var lastName: String?,
        var age: String?,
        var gender: String?,
        var country: String?
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

    abstract class BaseRequest {
        var httpStatusCodes: Boolean = true
        var targetEnv: String = "mobile"
    }

    interface Auth {
        var apiKey: String
        var userKey: String
        var secret: String
    }

    class Ids private constructor() {

        class GetAccountInfoRequest(
            var UID: UID,
            var regToken: RegistrationToken
        ): BaseRequest(), ParametersAdapter {

            override fun parameters(): Parameters {
                return mapOf(
                        Pair("httpStatusCodes", if (this.httpStatusCodes) "true" else "false")
                        , Pair("targetEnv", this.targetEnv)
                        , Pair("UID", this.UID)
                        , Pair("regToken", this.regToken)
                )
            }

        }

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
            var identities: List<Identity>,
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
            override var apiKey: String,
            override var userKey: String,
            override var secret: String
        ): BaseRequest(), Auth, ParametersAdapter {

            override fun parameters(): Parameters {
                return mapOf(
                        Pair("httpStatusCodes", if (this.httpStatusCodes) "true" else "false"),
                        Pair("targetEnv", this.targetEnv),
                        Pair("apiKey", this.apiKey),
                        Pair("userKey", this.userKey),
                        Pair("secret", this.secret)
                )
            }

        }

        class InitRegistrationResponse(
            var regToken: String
        )

        class RegisterRequest(
            override var apiKey: String,
            override var userKey: String,
            override var secret: String,
            var email: String,
            var password: String,
            var regToken: String
        ): BaseRequest(), Auth, ParametersAdapter {

            override fun parameters(): Parameters {
                return mapOf(
                        Pair("httpStatusCodes", if (this.httpStatusCodes) "true" else "false"),
                        Pair("targetEnv", this.targetEnv),
                        Pair("apiKey", this.apiKey),
                        Pair("userKey", this.userKey),
                        Pair("secret", this.secret),
                        Pair("email", email),
                        Pair("password", password),
                        Pair("regToken", regToken)
                )
            }

        }

        class SessionInfo(
            var sessionToken: String,
            var sessionSecret: String)

        class ResetPasswordRequest(
            override var apiKey: String,
            override var userKey: String,
            override var secret: String,
            var loginID: String?,
            var passwordResetToken: String?,
            var newPassword: String?
        ): BaseRequest(), Auth, ParametersAdapter {

            override fun parameters(): Parameters {
                val params = mutableMapOf(
                        Pair("httpStatusCodes", if (this.httpStatusCodes) "true" else "false")
                        , Pair("targetEnv", this.targetEnv)
                        , Pair("apiKey", this.apiKey)
                        , Pair("userKey", this.userKey))
                this.loginID?.let { params.put("loginId", it) }
                this.passwordResetToken?.let { params.put("passwordResetToken", it) }
                this.newPassword?.let { params.put("newPassword", it) }
                return params
            }

        }

        class ResetPasswordResponse(
        )

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
            var registered: Date?,
            var isActive: Boolean,
            var isVerified: Boolean,
            var verifiedTimestamp: Long?,
            var verified: Date?,
            var socialProviders: String,
            var profile: Profile?,
            var created: Date,
            var createdTimestamp: Long,
            var lastLogin: Date,
            var lastLoginTimestamp: Long,
            var lastUpdated: Date,
            var lastUpdatedTimestamp: Long,
            var oldestDataUpdated: Date,
            var oldestDataUpdatedTimestamp: Long
        )

        class LoginRequest (
            override var apiKey: String,
            override var userKey: String,
            override var secret: String,
            var loginID: String,
            var password: String
        ): BaseRequest(), Auth, ParametersAdapter {

            override fun parameters(): Parameters {
                return mapOf(
                    Pair("httpStatusCodes", if (this.httpStatusCodes) "true" else "false")
                    , Pair("targetEnv", this.targetEnv)
                    , Pair("apiKey", this.apiKey)
                    , Pair("userKey", this.userKey)
                    , Pair("secret", this.secret)
                    , Pair("loginID", this.loginID)
                    , Pair("password", this.password)
                )
            }
        }

        class HashSettings(
            var algorithm: String,
            var rounds: Int,
            var salt: String,
            var format: String
        )

        class Password(
            var hash: String,
            var hashSettings: HashSettings
        )

        class Emails(
            var verified: List<String>,
            var unverified: List<String>
        )

        class Coordinates(
            var lat: Double,
            var lon: Double
        )

        class Location(
            var country: String,
            var state: String,
            var city: String,
            var coordinates: Coordinates
        )

        class LoginID(
            var username: String,
            var emails: List<String>,
            var unverifiedEmails: List<String>
        )

        class LoginResponse(
            var newUser: Boolean?, // Indicates whether the user logging in is new. The parameter is returned only when it is set to "true", or when the user is missing the 'connectionIdentity' field in the DB. When 'RaaS' is enabled, If 'newUser == TRUE' and no required fields are missing, a 'SocialLeadToAccountNewUser' event is called and a new user is created. If account is pending verification, a 'SocializeLeadToAccountsPendingVerification' event is fired instead.
            var sessionInfo: SessionInfo?, // An object containg session information. The content of this object depends on the targetEnv parameter (see above). By default, if the targetEnv parameter is not set (your client environment is web), the sessionInfo object contains the the following string fields: cookieName and cookieValue.Please create a session cookie with the name and value specified by these fields. Alternatively, if  the targetEnv parameter is set to "mobile" (your client runs on a mobile), the sessionInfo object contains the the following string fields: sessionToken and sessionSecret. Please send these fields to your mobile client. On your client side, call GSAPI.setSession (using the Mobile SDK) to save them on the app's storage.
            var regToken: String?, // A ticket that is used to complete a registration process. The regToken is returned when there is a pending registration error, which occurs when the user did not complete the registration process, or there are missing fields in the user profile data that were defined as "required" in the Schema. The regToken is valid for one hour.
            var unverifiedEmails: List<String>?, // One or more unverified emails, in case there is a pending verification error.
            var UID: String, // The unique user ID. This user ID should be used for login verification. See User.UID for more information.
            var UIDSignature: String, // The signature that should be used for login verification. See User.UID for more information.
            var signatureTimestamp: String, // The GMT time of the response in UNIX time format, i.e., the number of seconds since Jan. 1st 1970. The timestamp should be used for login verification. See User.UID for more information.
            var created: String, // The time the account was created in ISO 8601 format, e.g., "1997-07-16T19:20:30Z".
            var createdTimestamp: Long, // The time the account was created in Unix time format including milliseconds (i.e., the number of seconds since Jan. 1st 1970 * 1000).
//            var data: JSON object // Custom data. Any data that you want to store regarding the user that isn't part of the Profile object.
            var emails: Emails?, // The email addresses belonging to the user. This includes the following fields: verified - an array of strings representing the user's verified email addresses unverified - an array of strings representing the user's unverified email addresses. Note: emails must be specified explicitly in the include parameter in order to be included in the response.
            var identities: List<Identity>?, // An array of Identity Objects, each object represents a user's social identity. Each Identity Object contains imported data from a social network that the user has connected to. Note: You must explicitly specify identities within the include parameter for them to be included in the response: identities-active , identities-all, or identities-global to return only active identities, all identities of a site, or all identities of a site group, respectively. Be advised that if a user registers to your site using a Social Identity, then goes through the Forgot Password flow, a Site Login is added to their account, however, a Site Identity is not. A Site Identity can only be created when accounts.setAccountInfo is called on the user's account.
            var iRank: Int?, // Influencer rank of the user. The iRank is a number between 0-99, which denotes the percentile location of the user in comparison to all other site users as a site influencer. For example, if a user's iRank equals 60, this means that 60% of the site users influence less than this user, or in other words, this user is in the top 40% of site influencers. The iRank is calculated based on the amount of exposure this user provides the site. The calculation is done for the past several months, where recent activities receive higher ranks. The iRank is per site (per API key), the same user may have different ranks for different domains. The iRank calculation uses the following parameters: The number of friends this user has in all the networks to which he is connected through this site. The number of times this user shared something in this site (per month). The number of click backs that were made as a result of this user's shares.
            var isActive: Boolean?, // Indicates whether the account is active. The account is active once the user creates it even without finalizing it. The account can be deactivated, but it will still be registered if the registration process has been finalized.
            var isLockedOut: Boolean?, // Indicates whether the account is currently locked out. This parameter is not included in the response by default, and is not returned at all from accounts.search. If you wish to include it in a response, specify it as a value of the include parameter.
            var isRegistered: Boolean?, // Indicates whether the user is registered. The user is registered once his registration has been finalized.
            var isVerified: Boolean?, // Indicates whether the account email is verified.
            var lastLogin: Date?, // The time of the last login of the user in ISO 8601 format, e.g., "1997-07-16T19:20:30Z".
            var lastLoginLocation: List<Location>?, // The user's last login location. This includes the following fields: country - a string representing the two-character country code. state - a string representing the state, where available. city - a string representing the city name. coordinates - an object containing: lat - a double representing the latitude of the center of the city. lon - a double representing the longitude of the center of the city.
            var lastLoginTimestamp: Long?, // The time of the last login of the user in Unix time format including milliseconds (i.e., the number of seconds since Jan. 1st 1970 * 1000).
            var lastUpdated: Date?, // The time when the account object was last updated (either full or partial update) in ISO 8601 format, e.g., "1997-07-16T19:20:30Z". This is updated when any change is made to the account, for example, when a password is changed or a user logs in.
            var lastUpdatedTimestamp: Long?, // The time when the last update of the object occurred (either full or partial update) in Unix time including milliseconds, based on when the 'lastUpdated', 'Report AccountsFirstLogin' or 'AccountsReturnedLogin' events are fired.
            var loginIDs: List<LoginID>?, // The user's login identifiers. This includes the following fields: username - a string representing the username emails - an array of strings representing email addresses unverifiedEmails - an array of strings representing email addresses that were not validated Note: loginIDs must be specified explicitly in the include parameter in order to be included in the response.
            var loginProvider: String?, // The name of the provider that the user used in order to login.
            var oldestDataUpdated: Date?, // The time when the oldest data of the object was refreshed in ISO 8601 format, e.g., "1997-07-16T19:20:30Z".
            var oldestDataUpdatedTimestamp: Long?, // The time when the oldest data of the object was refreshed in Unix time format including milliseconds (i.e., the number of seconds since Jan. 1st 1970 * 1000).
            var password: List<Password>?, // The user's Site account password details. Includes the following: hash - the hashed password hashSettings - object includes: algorithm - Represents the hash algorithm used to encrypt the password. rounds - Represents the number of iterations to perform the hashing. salt - Represents the BASE64 encoded value of the salt. format - Represents the template for merging clear-text passwords. This is only returned if the pwHashFormat parameter was set during account import and until the user's first login to Gigya (when the user's password is rehashed per the site's settings). See the RaaS Import Guide for additional information.
            var profile: Profile?, // The user's profile information as described in the object. The profile is returned in the response by default, but if the include parameter is used to specify other fields that should be provided in the response, the profile must also be specified explicitly in the include parameter.
            var rbaPolicy: String?, // The current RBA Policy defined for the specified user.
            var registered: Date?, // The time when the isRegistered parameter was set to true in ISO 8601 format, e.g., "1997-07-16T19:20:30Z".
            var registeredTimestamp: Long?, // The GMT time when the isRegistered parameter was set to true in UNIX time format, including milliseconds.
            var regSource: String?, // A string representing the source of the registration. Can be used to set varying destination pages in accounts.setPolicies.
            var socialProviders: String?, // A comma-separated list of the names of the providers to which the user is connected/logged in.
//            var subscriptions: Subscriptions Object // The user's subscription information.
            var verified: Date?, // The time when the isVerified parameter was set to true in ISO 8601 format, e.g., "1997-07-16T19:20:30Z".
            var verifiedTimestamp: Long? // The GMT time when the isVerified parameter was set to true in Unix time format including milliseconds (i.e., the number of seconds since Jan. 1st 1970 * 1000).
        )
    }
}
