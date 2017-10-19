package com.heb.dtn.flow

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.heb.dtn.adapter.ProductsAdapter
import com.heb.dtn.app.AppProxy
import com.heb.dtn.extensions.hide
import com.heb.dtn.flow.core.BaseFlowController
import com.heb.dtn.foundation.promise.android.always
import com.heb.dtn.foundation.promise.android.catch
import com.heb.dtn.foundation.promise.android.then
import com.heb.dtn.fragment.ProductsListFragment
import com.heb.dtn.service.domain.cart.Cart
import com.inmotionsoftware.imsflow.*
import kotlinx.android.synthetic.main.products_list_fragment.*

/**
 * Created by jcarbo on 10/18/17.
 */
class HomeFlowController(private val context: AppCompatActivity, fragmentContainerView: Int)
    : BaseFlowController<HomeFlowController.State, Unit, Unit>(context = context, fragmentContainerView = fragmentContainerView) {

    enum class State : FlowState {
        Begin,
        Browse,
        Detail,

        Fail,
        Cancel,
        Back
    }

    private var cart: Cart? = null
    private val prodListFragment: ProductsListFragment by lazy { ProductsListFragment() }

    init {
        AppProxy.proxy.serviceManager().cartService().createCart()
                .then {
                    this.cart = it
                }
        this.initialize()
    }

    override fun registerStates(states: Builder<State>) {
        states
                .initialState(state = State.Begin)
                .add(from = State.Begin, to = State.Browse)
                .add(from = State.Browse, to = State.Detail)
                .addFromAny(to = State.Back)
                .addFromAny(to = State.Fail)
                .addFromAny(to = State.Cancel)
    }

    override fun registerEvents(listener: StateListener<State>) {
        listener
                .on(state = State.Browse, execute = this::onBrowse)
                .onType(state = State.Back, execute = this::onBack)
                .onType(state = State.Cancel, execute = this::onBack)
                .onType(state = State.Fail, execute = this::onFail)
    }

    override fun onStart(args: Unit) {
        this.transition(from = State.Begin, to = State.Browse)
    }

    private fun onBrowse(state: State, with: Any?) {
        val indicator = this.showActivityIndicator()
        AppProxy.proxy.serviceManager().productService()
                .getProducts()
                .then {
                    it.items
                    indicator?.hide()
                    this.flow(fragment = prodListFragment, args = it.items)
                            .back { this.transition(from = state, to = State.Back) }
                            .cancel { this.transition(from = state, to = State.Cancel) }
                }
                .catch {
                    //TODO: (JC) show an error
                }
                .always {
                    indicator?.hide()
                }
    }

}