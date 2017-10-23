package com.heb.dtn.login

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.heb.dtn.R
import com.heb.dtn.extensions.domain.isValidEmail
import com.heb.dtn.flow.core.BaseFormFlowDialogFragment
import com.heb.dtn.service.api.AccountValidationFlags
import com.heb.dtn.utils.KeyboardUtils
import kotlinx.android.synthetic.main.fragment_create_account_email.*

/**
 * Fragment to allow the user to set a password for their account.
 */
class CreateAccountEmailFragment : BaseFormFlowDialogFragment<AccountValidationFlags, String>() {

    data class Result(val email: String)

    override val dialogTheme: Int = R.style.BaseCustomDialogFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_create_account_email, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.emailEditText.addTextChangedListener(this)

        this.nextButton.setOnClickListener {
            this.finish(result = this.emailEditText.text.toString())
        }
    }

    override fun afterTextChanged(s: Editable?) {
        this.nextButton.isEnabled = this.emailEditText.text.toString().isValidEmail
    }

    override fun finish(result: String) {
        KeyboardUtils.dismissKeyboard(context = this.context, view = this.nextButton)
        this.dismiss()
        super.finish(result = result)
    }

    override fun flowWillRun(args: AccountValidationFlags) {
        super.flowWillRun(args)
        KeyboardUtils.requestFocus(context = this.context, editText = this.emailEditText, delayMillis = KeyboardUtils.defaultDelayMillis)
    }

    override fun onPause() {
        KeyboardUtils.dismissKeyboard(context = this.context, view = this.nextButton)
        super.onPause()
    }

}
