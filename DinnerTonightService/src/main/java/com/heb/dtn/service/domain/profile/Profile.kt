package com.heb.dtn.service.domain.profile

import com.heb.dtn.service.domain.store.Store

//
// Created by Khuong Huynh on 9/27/17.
//

class Profile {
    var profileId: String? = null
    var firstName: String? = null
    var lastName: String? = null
    var prefix: String? = null
    var loginName: String? = null
    var screenName: String? = null
    var email: String? = null

    var preferredStore: String? = null
    var preferredStoreDetails: Store? = null
}
