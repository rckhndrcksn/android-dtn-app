package com.heb.dtn.login

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.heb.dtn.R
import com.heb.dtn.fragment.DTNFormDialogFragment
import kotlinx.android.synthetic.main.create_account_password_fragment.*

/**
 * Fragment to allow the user to set a password for their account.
 */
class CreateAccountPasswordDialogFragment(): DTNFormDialogFragment() {
    override val dialogTheme: Int = R.style.BaseCustomDialogFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.create_account_password_fragment, container, false)


    override fun afterTextChanged(s: Editable?) {
        next.isEnabled = !password.text.isNullOrBlank() && password.text.length >= 8 && password.text.contains(Regex("\\d"))
    }
}