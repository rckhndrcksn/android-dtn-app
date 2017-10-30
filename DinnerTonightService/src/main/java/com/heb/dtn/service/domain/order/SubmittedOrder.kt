package com.heb.dtn.service.domain.order

import com.heb.dtn.service.domain.catalog.Product
import java.util.*

//
// Created by Khuong Huynh on 10/30/17.
//

class SubmittedOrder(
    var items: List<Product>,
    var totalPrice: Float,
    var currentStatus: String,
    var orderSubmissionTime: Date
)

internal class SubmittedOrders(
    var items: List<SubmittedOrder>
)
