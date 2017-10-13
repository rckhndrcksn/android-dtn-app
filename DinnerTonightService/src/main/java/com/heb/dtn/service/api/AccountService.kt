package com.heb.dtn.service.api

import com.heb.dtn.service.domain.profile.Profile
import com.heb.dtn.service.domain.profile.ProfileId
import com.heb.dtn.service.domain.profile.RegistrationResult
import com.heb.dtn.service.domain.profile.forms.Registration
import com.heb.dtn.service.domain.profile.forms.UpdateProfile
import com.inmotionsoftware.promise.Promise

//
// Created by Khuong Huynh on 9/27/17.
//

interface AccountService {

    fun updateProfile(form: UpdateProfile): Promise<Boolean>

    fun changePassword(from: String, to: String, profileId: ProfileId): Promise<Boolean>

    fun getProfile(profileId: ProfileId): Promise<Profile>

    fun createAccount(form: Registration): Promise<RegistrationResult>

    fun resetPassword(email: String, phone: String, dob: String): Promise<Boolean>

    fun resetPassword(email: String): Promise<Boolean>

}
