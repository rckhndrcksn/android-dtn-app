package com.heb.dtn.fragment

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.heb.dtn.R
import com.heb.dtn.adapter.ProductsAdapter
import com.heb.dtn.app.AppProxy
import com.heb.dtn.foundation.promise.android.catch
import com.heb.dtn.foundation.promise.android.then
import com.heb.dtn.foundation.promise.android.thenp
import com.heb.dtn.foundation.service.JSONEncoder
import com.heb.dtn.service.domain.cart.Cart
import com.heb.dtn.service.domain.cart.CartProduct
import com.heb.dtn.service.domain.catalog.Product
import com.heb.dtn.widget.ProductView
import kotlinx.android.synthetic.main.products_list_fragment.*
import java.nio.charset.Charset

/**
 * Created by jcarbo on 10/9/17.
 */
class ProductsListFragment: Fragment(), ProductView.AddToCartListener {
    private var cart: Cart? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.products_list_fragment, container, false)
        setHasOptionsMenu(true)
        AppProxy.proxy.serviceManager().productService()
                .getProducts()
                .then {
                    if (it.items.isEmpty()) {
                        showErrorMessage()
                    }
                    setVisibility(loadingLayout, View.GONE)
                    setVisibility(recyclerView, View.VISIBLE)
                    setVisibility(errorMessage, View.GONE)
                    recyclerView.layoutManager = LinearLayoutManager(context)

                    recyclerView.adapter = ProductsAdapter(context, it.items.filter { it.romanceCopy != null }, this)
                }
                .catch {
                    showErrorMessage()
                }

        AppProxy.proxy.serviceManager().cartService().createCart()
                .then {
                    cart = it
                }
        return view
    }

    private fun showErrorMessage() {
        setVisibility(loadingLayout, View.GONE)
        setVisibility(recyclerView, View.GONE)
        setVisibility(errorMessage, View.VISIBLE)
    }

    private fun setVisibility(view: View, vis: Int) {
        view.visibility = vis
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.cart -> {
                activity.supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, CartListFragment(cart ?: Cart()))
                        .addToBackStack(null)
                        .commit()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun addProductToCart(product: Product) {
        val prodId = product.prodId
        val cartProd = cart?.products?.firstOrNull { it.productId == prodId}

        if (cartProd != null) {
            cartProd.quantity++
            updateCart(cartProd)
        } else {
            val prod = CartProduct(prodId, 1)
            updateCart(prod)
            val newList: MutableList<CartProduct> = mutableListOf()
            newList.addAll(cart?.products ?: emptyList())
            newList.add(prod)
            cart?.products = newList
        }
    }

    private fun updateCart(product: CartProduct) {
        AppProxy.proxy.serviceManager().cartService()
                .updateCart(cartId = cart!!.cartId, product = product)
                .then {
                    Toast.makeText(context, "Product added to cart", Toast.LENGTH_SHORT).show()
                }
                .catch {
                    Toast.makeText(context, "Unable to add product to cart", Toast.LENGTH_SHORT).show()
                }
    }
}