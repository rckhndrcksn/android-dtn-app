package com.heb.dtn.service.internal.dtn

import com.heb.dtn.foundation.service.HTTPService
import com.heb.dtn.foundation.service.post
import com.heb.dtn.service.api.OrderService
import com.heb.dtn.service.domain.cart.Cart
import com.heb.dtn.service.domain.order.OrderResult
import com.inmotionsoftware.promise.Promise

/**
 * Created by jcarbo on 10/13/17.
 */
class DinnerTonightOrderService(config: HTTPService.Config) : HTTPService(config = config),  OrderService {

    override fun createOrder(cart: Cart): Promise<OrderResult> =
            this.post(route = "api/v1/orders/submit", body = UploadBody.Json(cart.cartId), type = OrderResult::class.java)

}