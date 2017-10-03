package com.heb.dtn.service.internal.dtn

import com.heb.dtn.foundation.service.HTTPService
import com.heb.dtn.foundation.service.get
import com.heb.dtn.service.api.ProductService
import com.heb.dtn.service.domain.catalog.ProductSearchResult
import com.inmotionsoftware.promise.Promise

//
// Created by jcarbo on 9/29/17.
//

class DinnerTonightProductService(config: HTTPService.Config) : HTTPService(config = config), ProductService {

    override fun getProducts(): Promise<ProductSearchResult> {
        val route = "api/v1/products"
        return this.get(route = route, type = ProductSearchResult::class.java)
    }

}
