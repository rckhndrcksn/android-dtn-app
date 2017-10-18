package com.heb.dtn.login

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.heb.dtn.R
import com.heb.dtn.extensions.domain.isValidEmail
import com.heb.dtn.flow.core.BaseFormFlowDialogFragment
import com.heb.dtn.fragment.DTNFormDialogFragment
import com.heb.dtn.utils.KeyboardUtils
import kotlinx.android.synthetic.main.forgot_password_dialog.*

/**
 * DialogFragment used to reset the user's password.
 */
class ForgotPasswordDialogFragment: BaseFormFlowDialogFragment<String, String>() {
    override val dialogTheme: Int = R.style.PasswordRecoveryDialogStyle

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.forgot_password_dialog, container, false)

    override fun flowWillRun(args: String) {
        this.email.setText(args)
        this.email.addTextChangedListener(this)
        KeyboardUtils.requestFocus(context = this.context, editText = this.email, delayMillis = KeyboardUtils.defaultDelayMillis)

        this.resetPassword.setOnClickListener {
            KeyboardUtils.dismissKeyboard(context = this.context, view = this.email)
            this.finish(result = this.email.text.toString()) }

        updateUserInterface()
    }

    private fun updateUserInterface() {
        this.resetPassword.isEnabled = this.email.text.toString().isValidEmail
    }

    override fun finish(result: String) {
        KeyboardUtils.dismissKeyboard(context = this.context, view = this.email)
        this.dismiss()
        super.finish(result = result)
    }

    override fun onPause() {
        KeyboardUtils.dismissKeyboard(context = this.context, view = this.email)
        super.onPause()
    }

    override fun afterTextChanged(s: Editable?) {
        this.resetPassword.isEnabled = this.email.text.toString().isValidEmail
    }
}