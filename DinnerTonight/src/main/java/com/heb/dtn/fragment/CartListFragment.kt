package com.heb.dtn.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.heb.dtn.R
import com.heb.dtn.adapter.CartProductsAdapter
import com.heb.dtn.app.AppProxy
import com.heb.dtn.foundation.promise.android.catch
import com.heb.dtn.foundation.promise.android.then
import com.heb.dtn.service.domain.cart.Cart
import com.heb.dtn.service.domain.cart.CartProduct
import com.heb.dtn.service.domain.order.Order
import com.heb.dtn.widget.CartProductView
import kotlinx.android.synthetic.main.cart_list_fragment.*

/**
 * Created by jcarbo on 10/9/17.
 */
class CartListFragment: Fragment, CartProductView.Listener {
    private var cart: Cart = Cart()

    constructor(): super()

    constructor(cart: Cart) {
        this.cart = cart
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.cart_list_fragment, container, false)



    override fun onResume() {
        super.onResume()
        recyclerView.adapter = CartProductsAdapter(context, cart.cartId, cart.products, this)
        recyclerView.layoutManager = LinearLayoutManager(context)

        checkout.setOnClickListener {
            AppProxy.proxy.serviceManager().orderService().submitOrder(order = Order(cartId = cart.cartId, customerId = ""))
                    .then {
                        if (it.success) {
                            Toast.makeText(context, "order placed", Toast.LENGTH_SHORT).show()
                            cart = Cart()
                            recyclerView.adapter = CartProductsAdapter(context, cart.cartId, cart.products, this)
                        } else {
                            Toast.makeText(context, "order failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .catch { Toast.makeText(context, "order failed", Toast.LENGTH_SHORT).show() }
        }
    }

    override fun remove(product: CartProduct) {
        product.quantity = 0
        AppProxy.proxy.serviceManager().cartService()
               .updateCart(cartId = cart.cartId, cartProduct = product)
               .then {
                   Toast.makeText(context, "Item removed", Toast.LENGTH_SHORT).show()
                   cart = it
                   recyclerView.adapter = CartProductsAdapter(context, cart.cartId, cart.products, this)
               }
               .catch {
                   Toast.makeText(context, "Unable to remove item", Toast.LENGTH_SHORT).show()
               }
    }
}