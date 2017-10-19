package com.heb.dtn.flow

import android.support.v7.app.AppCompatActivity
import com.heb.dtn.flow.core.BaseFlowController
import com.heb.dtn.fragment.OrderListFragment
import com.inmotionsoftware.imsflow.*

/**
 * Created by jcarbo on 10/18/17.
 */
class OrdersFlowController(private val context: AppCompatActivity, fragmentContainerView: Int)
    : BaseFlowController<OrdersFlowController.State, Unit, Unit>(context = context, fragmentContainerView = fragmentContainerView) {

    enum class State : FlowState {
        Begin,
        List,
        Detail,
        Edit,
        Submit,

        Fail,
        Back,
        Cancel
    }

    private val orderListFragment: OrderListFragment by lazy { OrderListFragment() }

    init {
        this.initialize()
    }

    override fun registerStates(states: Builder<State>) {
        states
                .initialState(state = State.Begin)
                .add(from = State.Begin, to = State.List)
    }

    override fun registerEvents(listener: StateListener<State>) {
        listener
                .on(state = State.List, execute = this::onList)
                .onType(state = State.Back, execute = this::onBack)
                .onType(state = State.Cancel, execute = this::onBack)
                .onType(state = State.Fail, execute = this::onFail)
    }

    override fun onStart(args: Unit) {
        this.transition(from = State.Begin, to = State.List)
    }

    private fun onList(state: State, with: Any?) {
        this.flow(fragment = orderListFragment, args = Unit)
                .back { this.transition(from = state, to = State.Back) }
                .cancel { this.transition(from = state, to = State.Cancel) }
    }
}