package com.heb.dtn.service.domain.account

import com.heb.dtn.service.domain.profile.Profile

//
// Created by Khuong Huynh on 10/18/17.
//

class AuthResponse(
    var profile: Profile,
    var uid: UID,
    var uidSignature: String,
    var timestamp: String
)
