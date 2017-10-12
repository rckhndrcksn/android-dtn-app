package com.heb.dtn.service.domain.profile

import com.heb.dtn.service.domain.store.Store

//
// Created by Khuong Huynh on 9/27/17.
//

typealias UserId = String

class Profile(
    val userId: UserId,
    val firstName: String,
    val lastName: String,
    val email: String
)
