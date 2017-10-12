package com.heb.dtn.login

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.heb.dtn.R
import com.heb.dtn.fragment.DTNFormDialogFragment
import kotlinx.android.synthetic.main.create_account_name_fragment.*

/**
 * Fragment that allows the user to enter their first and last name and first screen in the create account flow.
 */
class CreateAccountNameDialogFragment(): DTNFormDialogFragment() {
    override val dialogTheme: Int = R.style.BaseCustomDialogFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.create_account_name_fragment, container, false)


    override fun afterTextChanged(s: Editable?) {
        next.isEnabled = !firstName.text.isNullOrBlank() && !lastName.text.isNullOrBlank()
    }
}