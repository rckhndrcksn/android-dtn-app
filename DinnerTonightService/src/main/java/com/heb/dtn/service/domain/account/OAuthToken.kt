package com.heb.dtn.service.domain.account

import com.heb.dtn.service.domain.profile.Profile

//
// Created by Khuong Huynh on 9/27/17.
//

open class OAuthToken {
    var refreshToken: String? = null
    var accessToken: String? = null
    var tokenType: String? = null
    var expiresIn: Int? = null
    var scope: String? = null
    var profile: Profile? = null
}
