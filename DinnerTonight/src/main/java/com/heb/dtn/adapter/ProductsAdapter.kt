package com.heb.dtn.adapter

import android.content.Context
import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.heb.dtn.service.domain.catalog.Product
import com.heb.dtn.widget.ProductView

/**
 * Created by jcarbo on 10/9/17.
 */
class ProductsAdapter(context: Context, data: List<Product>, listener: ProductView.AddToCartListener): RecyclerView.Adapter<ProductsAdapter.ProductViewHolder>() {
    private var products: List<Product> = data
    private var context: Context = context
    private val addToCartListener = listener

    override fun getItemCount(): Int = products.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ProductViewHolder {
        val prodView = ProductView(context = context)
        val vh = ProductViewHolder(prodView)

        return vh
    }

    override fun onBindViewHolder(holder: ProductViewHolder?, position: Int) {
        (holder!!.itemView as ProductView).bindProduct(products[position], addToCartListener)

    }

    inner class ProductViewHolder(itemView: ProductView) : RecyclerView.ViewHolder(itemView) {
        private var view: View = itemView
    }
}