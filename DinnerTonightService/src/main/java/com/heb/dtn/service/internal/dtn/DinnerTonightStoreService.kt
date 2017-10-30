package com.heb.dtn.service.internal.dtn

import com.heb.dtn.foundation.service.HTTPService
import com.heb.dtn.foundation.service.Parameters
import com.heb.dtn.foundation.service.get
import com.heb.dtn.service.api.StoreService
import com.heb.dtn.service.domain.store.Store
import com.heb.dtn.service.domain.store.StoreSearchResult
import com.inmotionsoftware.promise.Promise

//
// Created by jcarbo on 9/29/17.
//

internal class DinnerTonightStoreService(config: HTTPService.Config) : HTTPService(config = config), StoreService {

    override fun getStore(storeId: String): Promise<Store> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getStores(latitude: Double, longitude: Double, radius: Double?): Promise<StoreSearchResult> {
        val route = "api/v1/locate/distance"
        val params: Parameters = mapOf("latitude" to latitude.toString()
                                        , "longitude" to longitude.toString()
                                        , "miles" to (radius?.toString() ?: "5.0"))

        return this.get(route = route, query = params, type = StoreSearchResult::class.java)
    }

}
