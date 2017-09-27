package com.heb.dtn.service.internal.dtn

import com.heb.dtn.foundation.service.HTTPService
import com.heb.dtn.foundation.service.get
import com.heb.dtn.service.api.ProductService
import com.heb.dtn.service.domain.catalog.ProductSearchResult
import com.inmotionsoftware.promise.Promise

/**
 * Created by jcarbo on 9/29/17.
 */
class DinnerTonightProductService(config: HTTPService.Config) : HTTPService(config = config), ProductService {

    override fun getProducts(): Promise<ProductSearchResult> {
        val route = "api/v1/products"
        return this.get(route = route, type = ProductSearchResult::class.java)
    }

//    var context: Context? = null
//
//    constructor(context: Context) {
//        this.context = context
//    }
//
//    override fun findProducts(): ProductsResults? {
//        var returnValue: ProductsResults? = null
//        val stream = context?.assets?.open("product_contract_example_updated.json")
//        val decoder = JSONDecoder()
//        decoder.let {
//            returnValue = it.decode(type = ProductsResults::class.java, value = stream!!.readBytes())
//        }
//
//        return returnValue
//    }

}
