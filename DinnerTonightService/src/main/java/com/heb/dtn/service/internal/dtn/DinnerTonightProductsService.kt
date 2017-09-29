package com.heb.dtn.service.internal.dtn

import android.content.Context
import com.heb.dtn.foundation.service.JSONDecoder
import com.heb.dtn.service.api.ProductsService
import com.heb.dtn.service.domain.ProductsResults

/**
 * Created by jcarbo on 9/29/17.
 */
class DinnerTonightProductsService : ProductsService {
    var context: Context? = null

    constructor(context: Context) {
        this.context = context
    }

    override fun findProducts(): ProductsResults? {
        var returnValue: ProductsResults? = null
        val stream = context?.assets?.open("product_contract_example_updated.json")
        val decoder = JSONDecoder()
        decoder.let {
            returnValue = it.decode(type = ProductsResults::class.java, value = stream!!.readBytes())
        }

        return returnValue
    }
}