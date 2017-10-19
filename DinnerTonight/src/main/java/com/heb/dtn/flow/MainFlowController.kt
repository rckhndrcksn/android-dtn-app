package com.heb.dtn.flow

import android.support.v7.app.AppCompatActivity
import com.heb.dtn.app.AppProxy
import com.heb.dtn.flow.core.BaseFlowController
import com.inmotionsoftware.imsflow.*

/**
 * Created by jcarbo on 10/18/17.
 */
class MainFlowController(private val context: AppCompatActivity, fragmentContainerView: Int)
    : BaseFlowController<MainFlowController.State, Unit, Unit>(context = context, fragmentContainerView = fragmentContainerView) {

    enum class State : FlowState {
        Begin,
        Home,
        Browse,
        Orders,
        Profile,

        Fail,
        Cancel,
        Back
    }

    init {
        this.initialize()
    }

    override fun registerStates(states: Builder<State>) {
        states
                .initialState(state = State.Begin)
                .add(from = State.Begin, to = State.Home)
                .add(from = State.Home, to = State.Browse)
                .add(from = State.Home, to = State.Orders)
                .add(from = State.Home, to = State.Profile)

                .add(from = State.Browse, to = State.Home)
                .add(from = State.Browse, to = State.Orders)
                .add(from = State.Browse, to = State.Profile)

                .add(from = State.Orders, to = State.Home)
                .add(from = State.Orders, to = State.Browse)
                .add(from = State.Orders, to = State.Profile)

                .add(from = State.Profile, to = State.Home)
                .add(from = State.Profile, to = State.Browse)
                .add(from = State.Profile, to = State.Orders)

                .add(from = State.Home, to = State.Back)
                .add(from = State.Browse, to = State.Back)
                .add(from = State.Orders, to = State.Back)
                .add(from = State.Profile, to = State.Back)
    }

    override fun registerEvents(listener: StateListener<State>) {
        listener
                .on(state = State.Home, execute = this::onHome)
                .on(state = State.Browse, execute = this::onBrowse)
                .on(state = State.Orders, execute = this::onOrders)
                .on(state = State.Profile, execute = this::onProfile)
                .on(state = State.Back, execute = this::onBack)
    }

    override fun onStart(args: Unit) {
        this.transition(from = State.Begin, to = State.Home)
    }

    private fun onHome(state: State, with: Any?) {
        AppProxy.proxy.flow.home(context = context, fragmentContainerView = fragmentContainerId)
                .back { this.finish() }
                .cancel { this.finish() }
    }

    private fun onBrowse(state: State, with: Any?) {
        AppProxy.proxy.flow.browse(context = context, fragmentContainerView = fragmentContainerId)
                .back { this.transition(from = state, to = State.Home) }
                .cancel { this.transition(from = state, to = State.Home) }
    }

    private fun onOrders(state: State, with: Any?) {
        AppProxy.proxy.flow.order(context = context, fragmentContainerView = fragmentContainerId)
                .back { this.transition(from = state, to = State.Home) }
                .cancel { this.transition(from = state, to = State.Home) }
    }

    private fun onProfile(state: State, with: Any?) {
        AppProxy.proxy.flow.profile(context = context, fragmentContainerView = fragmentContainerId)
                .back { this.transition(from = state, to = State.Home) }
                .cancel { this.transition(from = state, to = State.Home) }
    }
}