package com.heb.dtn.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.heb.dtn.R
import com.heb.dtn.extensions.domain.isValidEmail
import com.heb.dtn.fragment.DTNFormDialogFragment
import com.heb.dtn.utils.KeyboardUtils
import kotlinx.android.synthetic.main.login_dialog.*

/**
 * DialogFragment used for the user to login.
 */
class LoginDialogFragment: DTNFormDialogFragment() {
    override val dialogTheme: Int = R.style.BaseCustomDialogFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.login_dialog, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        email.addTextChangedListener(this)
        password.addTextChangedListener(this)
        KeyboardUtils.requestFocus(activity, email)
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        this.login.isEnabled = this.email.text.toString().isValidEmail && this.password.text.toString().isNotEmpty()
    }
}