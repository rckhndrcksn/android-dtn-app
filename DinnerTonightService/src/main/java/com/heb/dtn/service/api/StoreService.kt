package com.heb.dtn.service.api

import com.heb.dtn.service.domain.store.Store
import com.heb.dtn.service.domain.store.StoreSearchResult
import com.inmotionsoftware.promise.Promise

//
// Created by Khuong Huynh on 9/27/17.
//

interface StoreService {

    fun getStore(storeId: String): Promise<Store>

    fun getStores(latitude: Double, longitude: Double, radius: Double?): Promise<StoreSearchResult>

}
