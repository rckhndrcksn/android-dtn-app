package com.heb.dtn.flow.account

import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.heb.dtn.app.AppProxy
import com.heb.dtn.extensions.hide
import com.heb.dtn.flow.core.BaseFlowController
import com.heb.dtn.foundation.extension.contains
import com.heb.dtn.foundation.extension.remove
import com.heb.dtn.foundation.promise.android.always
import com.heb.dtn.foundation.promise.android.catch
import com.heb.dtn.foundation.promise.android.recover
import com.heb.dtn.foundation.promise.android.then
import com.heb.dtn.login.CreateAccountEmailFragment
import com.heb.dtn.login.CreateAccountNameFragment
import com.heb.dtn.login.CreateAccountPasswordFragment
import com.heb.dtn.login.CreateAccountSuccessFragment
import com.heb.dtn.service.api.AccountServiceError
import com.heb.dtn.service.api.AccountValidationFlags
import com.inmotionsoftware.imsflow.*

//
// Created by Khuong Huynh on 10/13/17.
//
class CreateAccountFlowController(private val context: AppCompatActivity, private val fragmentContainerView: Int)
    : BaseFlowController<CreateAccountFlowController.State, Unit, Boolean>(context = context, fragmentContainerView = fragmentContainerView) {

    enum class State : FlowState {
        Begin
        , Name
        , Email
        , Password
        , Submit
        , Success
        , Fail
        , Done
        , Cancel
        , Back
    }

    private object Registration {
        var firstName: String = ""
        var lastName: String = ""
        var email: String = ""
        var password: String = ""
    }

    private val nameFragment: CreateAccountNameFragment by lazy { CreateAccountNameFragment() }
    private val emailFragment: CreateAccountEmailFragment by lazy { CreateAccountEmailFragment() }
    private val passwordFragment: CreateAccountPasswordFragment by lazy { CreateAccountPasswordFragment() }
    private val successFragment: CreateAccountSuccessFragment by lazy { CreateAccountSuccessFragment() }
    private val form = Registration
    private var validationFlags = AccountValidationFlags()

    init {
        this.initialize()
    }

    override fun registerStates(states: Builder<State>) {
        states.initialState(state = State.Begin)

                .add(from = State.Name, to = State.Back)

                // screen states
                .add(from = State.Begin, to = State.Name)
                .add(from = State.Name, to = State.Email)
                .add(from = State.Email, to = State.Password)
                .add(from = State.Password, to = State.Submit)

                // back
                .add(from = State.Name, to = State.Cancel)
                .add(from = State.Email, to = State.Name)
                .add(from = State.Password, to = State.Email)

                // submit form
                .add(from = State.Submit, to = State.Password)
                .add(from = State.Submit, to = State.Fail)
                .add(from = State.Submit, to = State.Done)
                .add(from = State.Submit, to = State.Success)

                // success
                .add(from = State.Success, to = State.Done)

                // recoverable error states
                .add(from = State.Submit, to = State.Name)
                .add(from = State.Submit, to = State.Email)
                .add(from = State.Submit, to = State.Password)

                .addFromAny(State.Cancel)
                .addFromAny(State.Fail)
    }

    override fun registerEvents(listener: StateListener<State>) {
        listener
                .on(state = State.Name, execute = this::onName)
                .on(state = State.Email, execute = this::onEmail)
                .on(state = State.Password, execute = this::onPassword)
                .on(state = State.Submit, execute = this::onSubmit)
                .onType(state = State.Success, execute = this::onSuccess)
                .on(state = State.Done, execute = this::onDone)
                .onType(state = State.Fail, execute = this::onFail)
                .on(state = State.Cancel, execute = this::onCancel)
                .on(state = State.Back, execute = this::onBack)
    }

    override fun onStart(args: Unit) {
        this.validationFlags.clear()
        this.transition(from = State.Begin, to = State.Name)
    }

    //
    // Private Methods
    //

    private fun onName(state: State, with: Any?) {
        this.flow(dialogFragment = this.nameFragment, args = this.validationFlags)
                .back { this.transition(from = state, to = State.Cancel) }
                .cancel { this.transition(from = state, to = State.Cancel) }
                .complete { (firstName, lastName) ->
                    this.form.firstName = firstName
                    this.form.lastName = lastName

                    this.validationFlags.remove(sets = listOf(AccountValidationFlags.firstName, AccountValidationFlags.lastName))
                    this.transition(from = state, to = State.Email)
                }
                .catch { this.transition(from= state, to = State.Fail, with = it) }
    }

    private fun onEmail(state: State, with: Any?) {
        this.flow(dialogFragment = this.emailFragment, args = this.validationFlags)
                .back { this.transition(from = state, to = State.Name) }
                .cancel { this.transition(from = state, to = State.Cancel) }
                .complete { email ->
                    this.form.email = email

                    this.validationFlags.remove(sets = listOf(AccountValidationFlags.email, AccountValidationFlags.emailInUse))
                    this.transition(from = state, to = State.Password)
                }
                .catch { this.transition(from= state, to = State.Fail, with = it) }
    }

    private fun onPassword(state: State, with: Any?) {
        this.flow(dialogFragment = this.passwordFragment, args = this.validationFlags)
                .back { this.transition(from = state, to = State.Email) }
                .cancel { this.transition(from = state, to = State.Cancel) }
                .complete { password ->
                    this.form.password = password
                    this.validationFlags.remove(set = AccountValidationFlags.password)
                    this.transition(from = state, to = State.Submit)
                }
                .catch { this.transition(from= state, to = State.Fail, with = it) }
    }

    private fun onSubmit(state: State, with: Any?) {
        val activityIndicator = this.showActivityIndicator()
        AppProxy.proxy.accountManager()
                .createAccount(firstName = this.form.firstName, lastName = this.form.lastName, email = this.form.email, password = this.form.password)
                .then {
                    this.validationFlags.clear()
                    this.transition(from = state, to = State.Success, with = this.form.email)
                }
                .recover { error ->
                    val accountError = error as? AccountServiceError ?: throw error
                    when (accountError) {
                        is AccountServiceError.Validation -> {
                            this.validationFlags = accountError.flags
                            if (this.validationFlags.contains(sets = listOf(AccountValidationFlags.firstName, AccountValidationFlags.lastName))) {
                                this.transition(from = state, to = State.Name)
                            } else if (this.validationFlags.contains(sets = listOf(AccountValidationFlags.email, AccountValidationFlags.emailInUse))) {
                                this.transition(from = state, to = State.Email)
                            } else if (this.validationFlags.contains(set = AccountValidationFlags.password)) {
                                this.transition(from = state, to = State.Password)
                            } else {
                                throw accountError
                            }
                        }
                        else -> throw accountError
                    }
                }
                .catch {
                    Toast.makeText(this.context, it.localizedMessage, Toast.LENGTH_SHORT).show()
                    this.transition(from = state, to = State.Password)
                }
                .always {
                    this.passwordFragment.dismiss()
                    activityIndicator?.hide()
                }
    }

    private fun onSuccess(state: State, email: String) {
        this.flow(dialogFragment = this.successFragment, args = email)
                .back { this.transition(from = state, to = State.Email) }
                .cancel { this.transition(from = state, to = State.Cancel) }
                .complete { this.transition(from = state, to = State.Done)  }
                .catch { this.transition(from= state, to = State.Fail, with = it) }
    }

    private fun onDone(state: State, args: Any?) {
        this.finish(true)
    }

}
