package com.heb.dtn.login

import com.heb.dtn.flow.core.BaseFlowActivity

/**
 * Activity used for the Login or CreateAccount flows.
 */
class LoginOrSignUpActivity : BaseFlowActivity() {
    companion object {
        const val CREATE_ACCOUNT_ACTION = "com.heb.dtn.login.CREATE_ACCOUNT"
    }

    override fun onStartFlow() {
    }
}