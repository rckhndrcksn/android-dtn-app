package com.heb.dtn.flow

import android.support.v7.app.AppCompatActivity
import com.heb.dtn.flow.core.BaseFlowController
import com.heb.dtn.fragment.ProfileFragment
import com.inmotionsoftware.imsflow.*

/**
 * Created by jcarbo on 10/18/17.
 */
class ProfileFlowController(private val context: AppCompatActivity, fragmentContainerView: Int)
    : BaseFlowController<ProfileFlowController.State, Unit, Unit>(context = context, fragmentContainerView = fragmentContainerView){

    enum class State : FlowState {
        Begin,
        Profile,
        Login,
        Edit,

        Fail,
        Cancel,
        Back
    }

    private val profileFragment: ProfileFragment by lazy { ProfileFragment() }

    init {
        this.initialize()
    }

    override fun registerStates(states: Builder<ProfileFlowController.State>) {
        states
                .initialState(state = State.Begin)
                .add(from = State.Begin, to = State.Profile)
    }

    override fun registerEvents(listener: StateListener<ProfileFlowController.State>) {
        listener
                .on(state = State.Login, execute = this::onProfile)
                .onType(state = State.Back, execute = this::onBack)
                .onType(state = State.Cancel, execute = this::onBack)
                .onType(state = State.Fail, execute = this::onFail)
    }

    override fun onStart(args: Unit) {
        this.transition(from = State.Begin, to = State.Profile)
    }

    private fun onProfile(state: State, with: Any?) {
        this.flow(fragment = profileFragment, args = Unit)
                .back { this.transition(from = state, to = State.Back) }
                .cancel { this.transition(from = state, to = State.Cancel) }
    }

}