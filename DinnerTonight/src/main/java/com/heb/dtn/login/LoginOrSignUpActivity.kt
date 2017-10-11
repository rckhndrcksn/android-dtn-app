package com.heb.dtn.login

import android.widget.Toast
import com.heb.dtn.flow.core.BaseFlowActivity

/**
 * Activity used for the Login or CreateAccount flows.
 */
class LoginOrSignUpActivity : BaseFlowActivity() {
    companion object {
        const val CREATE_ACCOUNT_ACTION = "com.heb.dtn.login.CREATE_ACCOUNT"
    }

    override fun onStartFlow() {
        if (intent.action == CREATE_ACCOUNT_ACTION) {
            Toast.makeText(this, "Create account flow", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Login flow", Toast.LENGTH_SHORT).show()
        }
    }
}