package com.heb.dtn.service.domain.catalog

//
// Created by jcarbo on 9/29/17.
//

typealias ProductId = Int

class Product {
    var prodId: ProductId = 0
    var upc: String? = null
    var displayName: String? = null
    var price: Float? = null
    var prepTime: String? = null
    var tag: String? = null
    var numberOfServings: String? = null
    var romanceCopy: String? = null
    var ingredientsText: String? = null
    var prepInstructions: String? = null
    var nutrition: String? = null
    var productImageThumbnailUri: String? = null
}

