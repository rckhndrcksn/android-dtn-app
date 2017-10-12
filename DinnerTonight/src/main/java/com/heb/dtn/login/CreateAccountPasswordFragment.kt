package com.heb.dtn.login

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.heb.dtn.R
import com.heb.dtn.flow.core.BaseFormFlowDialogFragment
import kotlinx.android.synthetic.main.fragment_create_account_password.*

/**
 * Fragment to allow the user to set a password for their account.
 */
class CreateAccountPasswordFragment: BaseFormFlowDialogFragment<Unit, String>() {

    override val dialogTheme: Int = R.style.BaseCustomDialogFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_create_account_password, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.passwordEditText.addTextChangedListener(this)

        this.nextButton.setOnClickListener {
            this.finish(result = this.passwordEditText.text.toString())
        }
        this.nextButton.isEnabled = false
    }

    override fun afterTextChanged(s: Editable?) {
        this.nextButton.isEnabled = !this.passwordEditText.text.isNullOrBlank()
                                    && this.passwordEditText.text.length >= 8
                                    && this.passwordEditText.text.contains(Regex("\\d"))
    }

}
