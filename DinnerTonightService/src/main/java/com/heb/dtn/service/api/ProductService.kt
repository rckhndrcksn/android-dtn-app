package com.heb.dtn.service.api

import com.heb.dtn.service.domain.catalog.ProductSearchResult
import com.heb.dtn.service.domain.catalog.StoreProductSearchResult
import com.heb.dtn.service.domain.store.StoreNumber
import com.inmotionsoftware.promise.Promise

//
// Created by jcarbo on 9/29/17.
//

interface ProductService {

    fun getProducts(): Promise<ProductSearchResult>

    fun getProducts(atStore: StoreNumber): Promise<StoreProductSearchResult>

}
