package com.heb.dtn.service.api

import com.heb.dtn.service.domain.cart.Cart
import com.heb.dtn.service.domain.order.OrderResult
import com.inmotionsoftware.promise.Promise

/**
 * Created by jcarbo on 10/13/17.
 */
interface OrderService {

    fun createOrder(cart: Cart): Promise<OrderResult>
}