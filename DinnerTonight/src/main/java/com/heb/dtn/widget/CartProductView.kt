package com.heb.dtn.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.heb.dtn.R
import com.heb.dtn.service.domain.cart.CartId
import com.heb.dtn.service.domain.cart.CartProduct
import kotlinx.android.synthetic.main.cart_product_view.view.*

/**
 * Created by jcarbo on 10/9/17.
 */
class CartProductView: LinearLayout {
    private var cartId: CartId? = null
    private var cartProd: CartProduct? = null
    private var listener: Listener? = null

    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        View.inflate(context, R.layout.cart_product_view, this)
    }

    fun bindView(prod: CartProduct, cartId: CartId, listener: Listener) {
        this.cartId = cartId
        this.cartProd = prod
        this.listener = listener
        quantity.text = "Qty. " + prod.quantity
        productId.text = prod.productId.toString()
        removeItem.setOnClickListener { listener.remove(prod) }
    }

    interface Listener {
        fun remove(product: CartProduct)
    }
}