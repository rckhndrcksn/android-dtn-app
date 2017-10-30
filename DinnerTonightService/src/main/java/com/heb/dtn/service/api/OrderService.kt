package com.heb.dtn.service.api

import com.heb.dtn.service.domain.order.Order
import com.heb.dtn.service.domain.order.OrderResult
import com.heb.dtn.service.domain.order.SubmittedOrder
import com.heb.dtn.service.domain.profile.UserId
import com.inmotionsoftware.promise.Promise

/**
 * Created by jcarbo on 10/13/17.
 */
interface OrderService {

    fun submitOrder(order: Order): Promise<OrderResult>

    fun orderHistory(userId: UserId): Promise<List<SubmittedOrder>>

}
