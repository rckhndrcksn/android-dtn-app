package com.heb.dtn.login

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.heb.dtn.R
import com.heb.dtn.extensions.domain.isValidEmail
import com.heb.dtn.fragment.DTNFormDialogFragment
import kotlinx.android.synthetic.main.forgot_password_dialog.*

/**
 * DialogFragment used to reset the user's password.
 */
class ForgotPasswordDialogFragment: DTNFormDialogFragment() {
    override val dialogTheme: Int = R.style.PasswordRecoveryDialogStyle

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.forgot_password_dialog, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.email.addTextChangedListener(this)
    }

    override fun afterTextChanged(s: Editable?) {
        this.resetPassword.isEnabled = this.email.text.toString().isValidEmail
    }
}