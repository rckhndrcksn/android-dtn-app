package com.heb.dtn.flow.fullfillment

import android.support.v7.app.AppCompatActivity
import com.heb.dtn.app.AppProxy
import com.heb.dtn.flow.core.BaseFlowController
import com.heb.dtn.foundation.promise.android.catch
import com.heb.dtn.locator.StoreLocatorOption
import com.heb.dtn.locator.domain.StoreItem
import com.heb.dtn.widget.DTNTabLayout
import com.inmotionsoftware.imsflow.*

/**
 * Created by jcarbo on 10/30/17.
 */
class FullfillmentFlowController(private val context: AppCompatActivity, private val fragmentContainerView: Int, private val tabLayout: DTNTabLayout)
    : BaseFlowController<FullfillmentFlowController.State, Unit, StoreItem>(context = context, fragmentContainerView = fragmentContainerView),
        DTNTabLayout.OnTabSelectedListener {

    enum class State : FlowState {
        Begin,
        Pickup,
        Delivery,

        Back,
        Done,
        Fail,
        Cancel
    }

    init {
        this.initialize()
        tabLayout.addOnTabSelectedListener(this)
    }

    override fun registerStates(states: Builder<State>) {
        states
                .initialState(state = State.Begin)
                .add(from = State.Begin, to = State.Pickup)
                .add(from = State.Pickup, to = State.Delivery)
                .add(from = State.Pickup, to = State.Back)
                .add(from = State.Delivery, to = State.Back)
                .addFromAny(to = State.Cancel)
                .addFromAny(to = State.Fail)
                .addFromAny(to = State.Done)
    }

    override fun registerEvents(listener: StateListener<State>) {
        listener
                .on(state = State.Pickup, execute = this::onPickup)
                .on(state = State.Delivery, execute = this::onDelivery)
                .on(state = State.Cancel, execute = this::onCancel)
                .on(state = State.Back, execute = this::onBack)
                .onType(state = State.Done, execute = this::onDone)
                .onType(state = State.Fail, execute = this::onFail)
    }

    override fun onStart(args: Unit) {
        this.transition(from = State.Begin, to = State.Pickup)
    }


    private fun onDelivery(state: State, with: Any?) {
        //TODO: (JC) Implement a delivery flow controller for this
    }

    private fun onPickup(state: State, with: Any?) {
        AppProxy.proxy.flow.pickupFullfillment(context = this.context, fragmentContainerView = this.fragmentContainerView, option = StoreLocatorOption.DEFAULT)
                .back { this.transition(from = state, to = State.Back) }
                .cancel { this.transition(from = state, to = State.Back) }
                .complete {store ->
                    this.transition(from = state, to = State.Done, with = store)
                }
                .catch { this.transition(from = state, to = State.Fail) }
    }

    override fun onTabSelected(tab: DTNTabLayout.Tab) {
        if (tab.position == 1) {
            this.transition(from = State.Begin, to = State.Pickup)
        } else {
            this.transition(from = State.Begin, to = State.Delivery)
        }
    }

    override fun onTabUnselected(tab: DTNTabLayout.Tab) {}

    override fun onTabReselected(tab: DTNTabLayout.Tab) {}

    private fun onDone(state: State, store: StoreItem){
        this.finish(result = store)
    }
}