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

    override fun getProducts(): Promise<ProductSearchResult>
        = this.get(route = "api/v1/products", type = ProductSearchResult::class.java)

}
