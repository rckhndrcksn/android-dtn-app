package com.heb.dtn.service.domain.order

import com.heb.dtn.service.domain.cart.CartId
import com.heb.dtn.service.domain.profile.UserId

//
// Created by Khuong Huynh on 10/30/17.
//

class Order (
    var cartId: CartId,
    var customerId: UserId
)

class OrderResult(
    var success: Boolean = false
)
