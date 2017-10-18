package com.heb.dtn.service.domain.account

//
// Created by Maksim Orlovich on 10/10/17.
//

class Registration(
    var token: RegistrationToken,
    var uid: UID,
    var email: String,
    var password: String
)
