package com.heb.dtn.service.api

import com.heb.dtn.service.domain.cart.Cart
import com.heb.dtn.service.domain.cart.CartId
import com.heb.dtn.service.domain.cart.CartProduct
import com.heb.dtn.service.domain.catalog.ProductId
import com.inmotionsoftware.promise.Promise

//
// Created by Khuong Huynh on 10/4/17.
//

interface CartService {

    fun createCart(): Promise<Cart>

    fun getCart(cartId: CartId): Promise<Cart>

    fun updateCart(cartId: CartId, cartProduct: CartProduct): Promise<Cart>

}

fun CartService.updateCart(cartId: CartId, productId: ProductId, quantity: Int) =
        this.updateCart(cartId = cartId, cartProduct = CartProduct(productId = productId, quantity = quantity))
