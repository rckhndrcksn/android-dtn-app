package com.heb.dtn.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.heb.dtn.service.domain.cart.CartId
import com.heb.dtn.service.domain.cart.CartProduct
import com.heb.dtn.widget.CartProductView

/**
 * Created by jcarbo on 10/9/17.
 */
class CartProductsAdapter(private val context: Context, id: CartId, data: List<CartProduct>, listener: CartProductView.Listener) : RecyclerView.Adapter<CartProductsAdapter.CartItemViewHolder>() {
    private val products: List<CartProduct> = data
    private val cartId: CartId = id
    private val removeListener: CartProductView.Listener = listener

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(holder: CartItemViewHolder?, position: Int) {
        (holder!!.itemView as CartProductView).bindView(products[position], cartId, removeListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CartItemViewHolder {
        val view = CartProductView(context = context)
        return CartItemViewHolder(view)
    }

    inner class CartItemViewHolder(itemView: CartProductView) : RecyclerView.ViewHolder(itemView) {
        private var view: View = itemView
    }
}