package com.heb.dtn.service.internal.dtn

import com.heb.dtn.foundation.service.HTTPService
import com.heb.dtn.foundation.service.get
import com.heb.dtn.foundation.service.post
import com.heb.dtn.service.api.CartService
import com.heb.dtn.service.domain.cart.Cart
import com.heb.dtn.service.domain.cart.CartProduct
import com.inmotionsoftware.promise.Promise

//
// Created by Khuong Huynh on 10/4/17.
//

class DinnerTonightCartService(config: HTTPService.Config) : HTTPService(config = config), CartService {

    override fun createCart(): Promise<Cart>
        = this.post(route = "api/v1/cart/new", body = UploadBody.Empty<Unit>(), type = Cart::class.java)

    override fun getCart(cartId: Long): Promise<Cart>
        = this.get(route = "api/v1/cart/$cartId", type = Cart::class.java)

    override fun updateCart(cartId: Long, product: CartProduct): Promise<Cart>
        = this.post(route = "api/v1/cart/$cartId", body = UploadBody.Json(product), type = Cart::class.java)

}
