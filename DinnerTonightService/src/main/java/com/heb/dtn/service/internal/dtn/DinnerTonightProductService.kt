package com.heb.dtn.service.internal.dtn

import com.heb.dtn.foundation.service.HTTPService
import com.heb.dtn.foundation.service.get
import com.heb.dtn.service.api.ProductService
import com.heb.dtn.service.domain.catalog.ProductSearchResult
import com.heb.dtn.service.domain.catalog.StoreProductSearchResult
import com.heb.dtn.service.domain.store.StoreNumber
import com.inmotionsoftware.promise.Promise

//
// Created by jcarbo on 9/29/17.
//

class DinnerTonightProductService(config: HTTPService.Config) : HTTPService(config = config), ProductService {

    override fun getProducts(): Promise<ProductSearchResult>
        = this.get(route = "api/v1/products", type = ProductSearchResult::class.java)

    override fun getProducts(atStore: StoreNumber): Promise<StoreProductSearchResult>
        = this.get(route = "api/v1/stores/$atStore/products", type = StoreProductSearchResult::class.java)
}
