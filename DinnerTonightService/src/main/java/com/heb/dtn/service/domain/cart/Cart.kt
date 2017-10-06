package com.heb.dtn.service.domain.cart

//
// Created by Khuong Huynh on 10/4/17.
//

typealias CartId = Long

class Cart {

    var cartId: CartId = 0
    var products: List<CartProduct> = emptyList()

}
