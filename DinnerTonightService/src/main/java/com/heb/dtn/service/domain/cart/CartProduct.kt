package com.heb.dtn.service.domain.cart

import com.heb.dtn.service.domain.catalog.ProductId

//
// Created by Khuong Huynh on 10/4/17.
//

class CartProduct {

    constructor() {}

    constructor(productId: ProductId, quantity: Int) {
        this.productId = productId
        this.quantity = quantity
    }

    var productId: ProductId = 0
    var quantity: Int = 0

}
