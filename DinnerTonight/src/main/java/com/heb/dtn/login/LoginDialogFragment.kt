package com.heb.dtn.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.heb.dtn.R
import com.heb.dtn.extensions.domain.isValidEmail
import com.heb.dtn.flow.core.BaseFormFlowDialogFragment
import com.heb.dtn.fragment.DTNFormDialogFragment
import com.heb.dtn.utils.KeyboardUtils
import com.inmotionsoftware.imsflow.FlowPromise
import kotlinx.android.synthetic.main.login_dialog.*

//
// DialogFragment used for the user to login.
//
class LoginDialogFragment: BaseFormFlowDialogFragment<String?, LoginDialogFragment.LoginAction>() {

    sealed class LoginAction {
        class Login(val email: String, val password: String) : LoginAction()
        class ForgotPassword(val email: String) : LoginAction()
    }

    override val dialogTheme: Int = R.style.BaseCustomDialogFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.login_dialog, container, false)

    override fun flowWillRun(args: String?) {
        args?.let { this.email.setText(args) }
        this.email.addTextChangedListener(this)
        this.password.addTextChangedListener(this)

        KeyboardUtils.requestFocus(context = this.context, editText = this.email, delayMillis = KeyboardUtils.defaultDelayMillis)

        this.login.setOnClickListener {
            KeyboardUtils.dismissKeyboard(context = this.context, view = this.email)
            KeyboardUtils.dismissKeyboard(context = this.context, view = this.password)
            this.finish(LoginAction.Login(email = this.email.text.toString(), password = this.password.text.toString())) }
        this.password.setOnClickListener { this.finish(LoginAction.ForgotPassword(this.email.text.toString())) }

        updateUserInterface()
    }

    override fun finish(result: LoginAction) {
        KeyboardUtils.dismissKeyboard(context = this.context, view = this.email)
        KeyboardUtils.dismissKeyboard(context = this.context, view = this.password)
        this.dismiss()
        super.finish(result = result)
    }

    override fun onPause() {
        KeyboardUtils.dismissKeyboard(context = this.context, view = this.email)
        KeyboardUtils.dismissKeyboard(context = this.context, view = this.password)
        super.onPause()
    }

    private fun updateUserInterface() {
        this.login.isEnabled = this.email.text.toString().isValidEmail && this.password.text.toString().isNotEmpty()
    }

    //
    // TextWatcher
    //

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        updateUserInterface()
    }
}