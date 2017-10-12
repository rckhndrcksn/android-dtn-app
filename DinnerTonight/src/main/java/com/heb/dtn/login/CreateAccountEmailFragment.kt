package com.heb.dtn.login

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.heb.dtn.R
import com.heb.dtn.flow.core.BaseFormFlowDialogFragment
import kotlinx.android.synthetic.main.fragment_create_account_email.*

/**
 * Fragment to allow the user to set a password for their account.
 */
class CreateAccountEmailFragment : BaseFormFlowDialogFragment<Unit, CreateAccountEmailFragment.Result>() {

    data class Result(val email: String, val promoOptIn: Boolean)

    override val dialogTheme: Int = R.style.BaseCustomDialogFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_create_account_email, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.emailEditText.addTextChangedListener(this)

        this.nextButton.setOnClickListener {
            this.finish(result = CreateAccountEmailFragment.Result(email = this.emailEditText.text.toString(), promoOptIn = this.promoOptIn.isChecked))
        }
        this.nextButton.isEnabled = false
    }

    override fun afterTextChanged(s: Editable?) {
        this.nextButton.isEnabled = !this.emailEditText.text.isNullOrBlank()
    }

    override fun finish(result: CreateAccountEmailFragment.Result) {
        this.dismiss()
        super.finish(result = result)
    }

}
