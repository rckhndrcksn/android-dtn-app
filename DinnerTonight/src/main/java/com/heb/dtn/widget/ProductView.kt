package com.heb.dtn.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.heb.dtn.R
import com.heb.dtn.app.AppProxy
import com.heb.dtn.foundation.promise.android.then
import com.heb.dtn.service.domain.catalog.Product
import kotlinx.android.synthetic.main.product_view.view.*

/**
 * Created by jcarbo on 10/9/17.
 */
class ProductView: LinearLayout {
    var product: Product? = null
    var cartListener: AddToCartListener? = null

    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        View.inflate(context, R.layout.product_view, this)
    }

    fun bindProduct(data: Product?, listener: AddToCartListener) {
        this.product = data
        this.cartListener = listener
        updateView()
    }

    private fun updateView() {
        if (product == null) {
            productTitle.text = ""
            productDescription.text = ""
            cartAction.setOnClickListener(null)
        } else {
            productTitle.text = this.product?.displayName
            productDescription.text = this.product?.romanceCopy
            cartAction.setOnClickListener({
                this@ProductView.cartListener?.addProductToCart(product!!)
            })
        }
    }

    interface AddToCartListener {
        fun addProductToCart(product: Product)
    }
}