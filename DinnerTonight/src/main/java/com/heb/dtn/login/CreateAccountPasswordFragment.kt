package com.heb.dtn.login

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.heb.dtn.R
import com.heb.dtn.flow.core.BaseFormFlowDialogFragment
import com.heb.dtn.service.api.AccountValidationFlags
import com.heb.dtn.utils.DTNURLSpan
import com.heb.dtn.utils.KeyboardUtils
import kotlinx.android.synthetic.main.fragment_create_account_password.*

/**
 * Fragment to allow the user to set a password for their account.
 */
class CreateAccountPasswordFragment: BaseFormFlowDialogFragment<AccountValidationFlags, String>() {

    override val dialogTheme: Int = R.style.BaseCustomDialogFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_create_account_password, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.passwordEditText.addTextChangedListener(this)
        this.submitButton.setOnClickListener {
            this.finish(result = this.passwordEditText.text.toString())
        }

        val tppLink = getString(R.string.tpp_link)
        val tppTitle = getString(R.string.tpp_title)
        val startIndex = tppTitle.indexOf(tppLink)

        if (startIndex != -1) {
            this.tppTextView.movementMethod = LinkMovementMethod.getInstance()
            val ss = SpannableString(tppTitle)
            val span = DTNURLSpan("https://www.heb.com/static-page/article-template/privacy-policy", Color.BLACK)
            ss.setSpan(span, startIndex, startIndex + tppLink.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            this.tppTextView.text = ss
        }
    }

    override fun flowWillRun(args: AccountValidationFlags) {
        super.flowWillRun(args)
        KeyboardUtils.requestFocus(context, this.passwordEditText)
    }

    override fun afterTextChanged(s: Editable?) {
        val input = this.passwordEditText.text
        this.submitButton.isEnabled = !input.isNullOrBlank()
    }

    override fun onPause() {
        KeyboardUtils.dismissKeyboard(this.context, this.submitButton)
        super.onPause()
    }
}
