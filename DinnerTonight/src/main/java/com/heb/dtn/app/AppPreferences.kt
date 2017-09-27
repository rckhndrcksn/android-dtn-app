package com.heb.dtn.app

import android.content.Context
import android.content.SharedPreferences

//
// Created by Khuong Huynh on 9/27/17.
//

class AppPreferences(private val context: Context) {

    private val KEY_FIRST_LAUNCH = "FirstLaunch"
    private val KEY_SESSION_INFO = "SessionInfo"
    private val KEY_CRP_SLT = "CRP_SLT"

    private val preferences: SharedPreferences = this.context.getSharedPreferences("DinnerTonightAppPreferences", Context.MODE_PRIVATE)

    var isFirstLaunch: Boolean
        get() = this.preferences.getBoolean(KEY_FIRST_LAUNCH, true)
        set(flag) {
            val editor = this.preferences.edit()
            editor.putBoolean(KEY_FIRST_LAUNCH, flag)
            editor.apply()
        }

    fun sessionInfo(): String = this.preferences.getString(KEY_SESSION_INFO, "")

    fun setSessionInfo(session: String?) {
        val editor = this.preferences.edit()
        editor.putString(KEY_SESSION_INFO, session)
        editor.apply()
    }

    fun crpSlt(): String = this.preferences.getString(KEY_CRP_SLT, "")

    fun setCrpSlt(value: String) {
        val editor = this.preferences.edit()
        editor.putString(KEY_CRP_SLT, value)
        editor.apply()
    }

}
