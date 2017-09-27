package com.heb.dtn.service.api

import com.heb.dtn.service.domain.catalog.ProductSearchResult
import com.inmotionsoftware.promise.Promise

/**
 * Created by jcarbo on 9/29/17.
 */
interface ProductService {

    fun getProducts(): Promise<ProductSearchResult>

}
