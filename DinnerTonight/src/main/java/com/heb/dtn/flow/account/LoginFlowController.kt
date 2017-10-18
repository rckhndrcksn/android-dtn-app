package com.heb.dtn.flow.account

import android.support.v7.app.AppCompatActivity
import android.view.View
import com.heb.dtn.app.AppProxy
import com.heb.dtn.extensions.hide
import com.heb.dtn.flow.core.BaseFlowController
import com.heb.dtn.foundation.promise.android.*
import com.heb.dtn.fragment.StandardDialog
import com.heb.dtn.login.ForgotPasswordDialogFragment
import com.heb.dtn.login.LoginDialogFragment
import com.heb.dtn.login.LoginErrorDialogFragment
import com.heb.dtn.login.PasswordSentDialogFragment
import com.heb.dtn.utils.UIControlDelegation
import com.inmotionsoftware.imsflow.*

/**
 * Created by jcarbo on 10/17/17.
 */
class LoginFlowController(private val context: AppCompatActivity, private val fragmentContainerView: Int)
    : BaseFlowController<LoginFlowController.State, Unit, Boolean>(context = context, fragmentContainerView = fragmentContainerView) {

    enum class State : FlowState {
        Begin
        , Prompt
        , ForgotPassword
        , Submit
        , LoginError
        , SubmitPasswordReset
        , PasswordResetSuccess

        , Done
        , Cancel
        , Back
        , Fail
    }

    private val loginFragment: LoginDialogFragment by lazy { LoginDialogFragment() }
    private val loginErrorFragment: LoginErrorDialogFragment by lazy { LoginErrorDialogFragment() }
    private val forgotPasswordFragment: ForgotPasswordDialogFragment by lazy { ForgotPasswordDialogFragment() }
    private val sentForgotPasswordFragment: PasswordSentDialogFragment by lazy { PasswordSentDialogFragment() }

    init {
        this.initialize()
    }

    override fun registerStates(states: Builder<State>) {
        states
                .initialState(State.Begin)
                .add(from = State.Begin, to = State.Prompt)

                // Prompt screen
                .add(from = State.Prompt, to = State.Back)
                .add(from = State.Prompt, to = State.Submit)
                .add(from = State.Prompt, to = State.ForgotPassword)

                // Submit login data
                .add(from = State.Submit, to = State.Done)
                .add(from = State.Submit, to = State.LoginError)

                // Login error state
                .add(from = State.LoginError, to = State.Prompt)

                // Forgot password
                .add(from = State.ForgotPassword, to = State.Prompt)
                .add(from = State.ForgotPassword, to = State.SubmitPasswordReset)

                // Submit password reset data
                .add(from = State.SubmitPasswordReset, to = State.PasswordResetSuccess)
                .add(from = State.SubmitPasswordReset, to = State.ForgotPassword)

                // Password reset email sent
                .add(from = State.SubmitPasswordReset, to = State.Prompt)

                .addFromAny(to = State.Cancel)
                .addFromAny(to = State.Fail)
    }

    override fun registerEvents(listener: StateListener<State>) {
        listener
                .onNullableType(state = State.Prompt, execute = this::onPrompt)
                .onType(state = State.ForgotPassword, execute = this::onForgotPassword)
                .onType(state = State.Submit, execute = this::onSubmit)
                .on(state = State.LoginError, execute = this::onLoginError)

                .onType(state = State.SubmitPasswordReset, execute = this::onSubmitPasswordReset)
                .onType(state = State.PasswordResetSuccess, execute = this::onPasswordResetSuccess)
                .on(state = State.Back, execute = this::onBack)
                .on(state = State.Cancel, execute = this::onCancel)
                .on(state = State.Done, execute = this::onDone)
                .onType(state = State.Fail, execute = this::onFail)
    }

    override fun onStart(args: Unit) {
        this.transition(from = State.Begin, to = State.Prompt)
    }

    private fun onPrompt(state: State, email: String?) {
        this.flow(dialogFragment = this.loginFragment, args = email)
                .back { this.transition(from = state, to = State.Back) }
                .cancel { this.transition(from = state, to = State.Cancel) }
                .complete { action ->
                    when(action) {
                        is LoginDialogFragment.LoginAction.Login -> {
                            this.transition(from = state, to = State.Submit, with = Pair(action.email, action.password))
                        }
                        is LoginDialogFragment.LoginAction.ForgotPassword -> {
                            this.transition(from = state, to = State.ForgotPassword, with = action.email)
                        }
                    }
                }
                .catch { this.transition(from = state, to = State.Fail, with = it) }
    }

    private fun onSubmit(state: State, form: Pair<String, String>) {
        val activityIndicator = this.showActivityIndicator()

        val (email, password) = form
        AppProxy.proxy.accountManager()
                .login(email = email, password = password)
                .then {
                    this.transition(from = state, to = State.Done)
                }
                .catch {
                    this.transition(from = state, to = State.LoginError)
                }
                .always { activityIndicator?.hide() }
    }

    private fun onLoginError(state: State, with: Any?) {
        this.flow(dialogFragment = this.loginErrorFragment, args = Unit)
                .always { this.transition(from = state, to = State.Prompt) }
    }

    private fun onForgotPassword(state: State, email: String) {
        this.flow(dialogFragment = this.forgotPasswordFragment, args = email)
                .back { this.transition(from = state, to = State.Prompt) }
                .cancel { this.transition(from = state, to = State.Cancel) }
                .complete { result ->
                    this.transition(from = state, to = State.SubmitPasswordReset, with = result) }
                .catch { this.transition(from = state, to = State.Fail) }
    }

    private fun onSubmitPasswordReset(state: State, email: String) {
        val activityIndicator = this.showActivityIndicator()

        AppProxy.proxy.accountManager().forgotPassword(email)
                .then {
                    this.transition(from = state, to = State.PasswordResetSuccess, with = email)
                }
//                .recover {
//                    print("foobar")
//                    var dialog: StandardDialog? = null
//                    dialog = StandardDialog.Builder(this.context, R.style.LoginErrorStyle)
//                                        .setTitle("Error")
//                                        .setMessage("The email you entered appears to be invalid")
//                                        .setPositiveButton(R.string.ok_button, View.OnClickListener {
//                                            //resolve(Unit)
//                                        }).build()
//                               dialog?.isCancelable = false
//                                dialog?.show(this.controlActivity?.supportFragmentManager, UIControlDelegation.DIALOG_FRAGMENT_TAG)
//                }
                .catch {
                    this.transition(from = state, to = State.ForgotPassword, with = email)
                }
                .always { activityIndicator?.hide() }
/*
                .back { this.transition(from = state, to = State.Prompt) }
                .cancel { this.transition(from = state, to = State.Cancel) }
                .complete {
                    this.transition(from = state, to = State.Prompt) }
                .catch {
                    this.transition(from = state, to = State.Fail)
                }
                */
    }

    private fun onPasswordResetSuccess(state: State, email: String) {
        this.flow(dialogFragment = this.sentForgotPasswordFragment, args = email)
                .always { this.transition(from = state, to = State.Prompt, with = email) }
    }

    private fun onDone(state: State, with: Any?) {
        this.finish(result = true)
    }
}