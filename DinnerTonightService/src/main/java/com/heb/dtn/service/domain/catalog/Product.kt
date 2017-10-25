package com.heb.dtn.service.domain.catalog

import com.heb.dtn.service.domain.store.StoreNumber
import java.util.*

//
// Created by jcarbo on 9/29/17.
//

typealias ProductId = Long

class Product {
    var prodId: ProductId = 0
    var upc: Long? = null
    var displayName: String? = null
    var price: Float? = null
    var prepTime: String? = null
    var tag: String? = null
    var numberOfServings: String? = null
    var romanceCopy: String? = null
    var ingredientsText: String? = null
    var prepInstructions: String? = null
    //var nutritionFacts: String? = null
    var productImageThumbnailUri: String? = null
}

class StoreProduct {
    var productId: ProductId = 0
    var upc: Long = 0
    var displayName: String = ""
    var prepTime: String = ""
    var tag: String = ""
    var numberOfServings: String = ""
    var romanceCopy: String = ""
    var ingredientsText: String = ""
    var prepInstructions: String = ""
    //var nutritionFacts: String?
    var productImageThumbnailUri: String = ""
    var storeNumber: StoreNumber = 0
    var skuId: Long = 0
    var retailPrice: Double = 0.0
    var isAvailable: Boolean = false
    var retailEffectiveDate: Date = Date()
    var retailEndDate: Date = Date()
}
