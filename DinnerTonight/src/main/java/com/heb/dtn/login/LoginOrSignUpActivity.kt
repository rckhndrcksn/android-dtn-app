package com.heb.dtn.login

import com.heb.dtn.R
import com.heb.dtn.flow.core.BaseFlowActivity

/**
 * Activity used for the Login or CreateAccount flows.
 */
class LoginOrSignUpActivity : BaseFlowActivity() {
   override val layoutResId: Int = R.layout.login_create_account_activity

    companion object {
        const val CREATE_ACCOUNT_ACTION = "com.heb.dtn.login.CREATE_ACCOUNT"
    }

    override fun onStartFlow() {
        if (intent.action == CREATE_ACCOUNT_ACTION) {
            CreateAccountNameDialogFragment().show(supportFragmentManager, "CreateAccountNameDialogFragment")
        } else {
            LoginDialogFragment().show(supportFragmentManager, "LoginDialogFragment")
        }
    }
}