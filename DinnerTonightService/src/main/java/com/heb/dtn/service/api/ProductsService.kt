package com.heb.dtn.service.api

import com.heb.dtn.service.domain.ProductsResults

/**
 * Created by jcarbo on 9/29/17.
 */
interface ProductsService {
    fun findProducts(): ProductsResults?
}