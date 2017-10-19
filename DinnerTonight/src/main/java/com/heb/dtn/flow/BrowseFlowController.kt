package com.heb.dtn.flow

import android.support.v7.app.AppCompatActivity
import com.heb.dtn.flow.core.BaseFlowController
import com.heb.dtn.fragment.BrowseFragment
import com.inmotionsoftware.imsflow.*

/**
 * Created by jcarbo on 10/18/17.
 */
class BrowseFlowController(private val context: AppCompatActivity, fragmentContainerView: Int)
    : BaseFlowController<BrowseFlowController.State, Unit, Unit>(context = context, fragmentContainerView = fragmentContainerView) {

    enum class State : FlowState {
        Begin,
        Browse,
        Detail,
        Filter,

        Fail,
        Cancel,
        Back
    }

    private val browseListFragment: BrowseFragment by lazy { BrowseFragment() }

    init {
        this.initialize()
    }

    override fun registerStates(states: Builder<State>) {
        states
                .initialState(state = State.Begin)
                .add(from = State.Begin, to = State.Browse)
    }

    override fun registerEvents(listener: StateListener<State>) {
        listener
                .onType(state = State.Back, execute = this::onBack)
                .onType(state = State.Cancel, execute = this::onBack)
                .onType(state = State.Fail, execute = this::onFail)
    }

    override fun onStart(args: Unit) {
        this.transition(from = State.Begin, to = State.Browse)
    }

    private fun onBrowse(state: State, with: Any?) {
        this.flow(fragment = browseListFragment, args = Unit)
                .back { this.transition(from = state, to = State.Back) }
                .cancel { this.transition(from = state, to = State.Cancel) }
    }
}