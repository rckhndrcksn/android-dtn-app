package com.heb.dtn.service.domain.account

import com.heb.dtn.service.api.RegistrationToken
import com.heb.dtn.service.api.UID

//
// Created by Maksim Orlovich on 10/10/17.
//

class Registration(
    var token: RegistrationToken,
    var uid: UID,
    var email: String,
    var password: String
)
