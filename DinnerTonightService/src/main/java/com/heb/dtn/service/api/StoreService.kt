package com.heb.dtn.service.api

import com.heb.dtn.service.domain.StoreResults

/**
 * Created by jcarbo on 9/29/17.
 */
interface StoreService {
    fun locateStore(lat: Double, lon: Double, radius: Int): StoreResults
}