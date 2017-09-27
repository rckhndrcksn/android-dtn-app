package com.heb.dtn.service.internal.dtn.mock

import android.content.Context
import com.heb.dtn.foundation.promise.android.PromiseDispatch
import com.heb.dtn.foundation.service.JSONDecoder
import com.heb.dtn.service.api.ProductService
import com.heb.dtn.service.domain.catalog.ProductSearchResult
import com.inmotionsoftware.promise.Promise

//
// Created by Khuong Huynh on 10/2/17.
//

class MockDinnerTonightProductService(private val context: Context) : ProductService {

    override fun getProducts(): Promise<ProductSearchResult> {
        return Promise(Unit)
                .then(on = PromiseDispatch.BACKGROUND.executor) {
                    val stream = this.context.assets.open("product_contract_example_updated.json")
                    val result = JSONDecoder().decode(type = ProductSearchResult::class.java, value = stream.readBytes())
                    if (result == null) throw IllegalArgumentException()
                    result!!
                }
    }

}
