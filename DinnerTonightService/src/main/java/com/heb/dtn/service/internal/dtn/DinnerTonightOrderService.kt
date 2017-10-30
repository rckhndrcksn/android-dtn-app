package com.heb.dtn.service.internal.dtn

import com.heb.dtn.foundation.promise.android.then
import com.heb.dtn.foundation.service.HTTPService
import com.heb.dtn.foundation.service.get
import com.heb.dtn.foundation.service.post
import com.heb.dtn.service.api.OrderService
import com.heb.dtn.service.domain.order.Order
import com.heb.dtn.service.domain.order.OrderResult
import com.heb.dtn.service.domain.order.SubmittedOrder
import com.heb.dtn.service.domain.order.SubmittedOrders
import com.heb.dtn.service.domain.profile.UserId
import com.inmotionsoftware.promise.Promise

/**
 * Created by jcarbo on 10/13/17.
 */
internal class DinnerTonightOrderService(config: HTTPService.Config) : HTTPService(config = config),  OrderService {

    override fun submitOrder(order: Order): Promise<OrderResult>
            = this.post(route = "api/v1/orders/submit", body = UploadBody.Json(order), type = OrderResult::class.java)

    override fun orderHistory(userId: UserId): Promise<List<SubmittedOrder>>
            = this.get(route = "api/v1/$userId/orders", type = SubmittedOrders::class.java)
                  .then { it.items }

}
