package com.heb.dtn.flow.app

import android.support.v7.app.AppCompatActivity
import com.heb.dtn.app.AppProxy
import com.heb.dtn.flow.core.BaseFlowController
import com.heb.dtn.foundation.promise.android.catch
import com.heb.dtn.login.LoginCreateAccountSelectionFragment
import com.inmotionsoftware.imsflow.*

//
// Created by Khuong Huynh on 10/13/17.
//

class AppLandingFlowController(private val context: AppCompatActivity, private val fragmentContainerView: Int)
    : BaseFlowController<AppLandingFlowController.State, Unit, Unit>(context = context, fragmentContainerView = fragmentContainerView) {

    enum class State : FlowState {
        Begin
        , PromptSelection
        , Login
        , SignUp

        , Done
        , Fail
        , Back
    }

    private val selectionFragment: LoginCreateAccountSelectionFragment by lazy { LoginCreateAccountSelectionFragment() }

    init {
        this.initialize()
    }

    override fun registerStates(states: Builder<State>) {
        states.initialState(state = State.Begin)

                // screen states
                .add(from = State.Begin, to = State.PromptSelection)
                .add(from = State.PromptSelection, to = State.Login)
                .add(from = State.PromptSelection, to = State.Back)
                .add(from = State.PromptSelection, to = State.SignUp)

                .add(from = State.Login, to = State.Done)
                .add(from = State.SignUp, to = State.Done)

                .addFromAny(to = State.Fail)
    }

    override fun registerEvents(listener: StateListener<State>) {
        listener
                .on(state = State.PromptSelection, execute = this::onPromptSelection)
                .on(state = State.Login, execute = this::onLogin)
                .on(state = State.SignUp, execute = this::onSignUp)
                .on(state = State.Back, execute = this::onBack)
                .onType(state = State.Done, execute = this::onDone)
                .onType(state = State.Fail, execute = this::onFail)
    }

    override fun onStart(args: Unit) {
        this.transition(from = State.Begin, to = State.PromptSelection)
    }

    //
    // Private Methods
    //

    private fun onPromptSelection(state: State, with: Any?) {
        this.flow(dialogFragment = this.selectionFragment, args = Unit)
            .back { this.transition(from = state, to = State.Back, with = Unit) }
            .cancel { this.transition(from = state, to = State.Back, with = Unit) }
            .complete { result ->
                when (result) {
                    is LoginCreateAccountSelectionFragment.Result.Login -> {
                        this.transition(from = state, to = State.Login)
                    }
                    is LoginCreateAccountSelectionFragment.Result.SignUp -> {
                        this.transition(from = state, to = State.SignUp)
                    }
                    is LoginCreateAccountSelectionFragment.Result.AsGuest -> {
                        this.transition(from = state, to = State.Done)
                    }
                }
            }
            .catch { this.transition(from = state, to = State.Fail) }
    }

    private fun onLogin(state: State, with: Any?) {
        AppProxy.proxy.flow.login(context = this.context, fragmentContainerView = this.fragmentContainerView)
                .back { this.transition(from = state, to = State.PromptSelection) }
                .cancel { this.transition(from = state, to = State.PromptSelection) }
                .complete { this.transition(from = state, to = State.Done) }
                .catch { this.transition(from = state, to = State.Fail) }
    }

    private fun onSignUp(state: State, with: Any?) {
        AppProxy.proxy.flow.createAccount(context = this.context, fragmentContainerView = this.fragmentContainerView)
                .back { this.transition(from = state, to = State.PromptSelection) }
                .cancel { this.transition(from = state, to = State.PromptSelection) }
                .complete { this.transition(from = state, to = State.Done) }
                .catch { this.transition(from = state, to = State.Fail) }
    }

    private fun onDone(state: State, result: Any?) {
        this.finish(result = Unit)
    }

}
