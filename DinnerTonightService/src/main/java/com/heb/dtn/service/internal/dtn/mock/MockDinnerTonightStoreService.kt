package com.heb.dtn.service.internal.dtn.mock

import android.content.Context
import com.heb.dtn.foundation.promise.android.PromiseDispatch
import com.heb.dtn.foundation.service.JSONDecoder
import com.heb.dtn.service.api.ProductService
import com.heb.dtn.service.api.StoreService
import com.heb.dtn.service.domain.catalog.ProductSearchResult
import com.heb.dtn.service.domain.store.Store
import com.heb.dtn.service.domain.store.StoreSearchResult
import com.inmotionsoftware.promise.Promise

//
// Created by Khuong Huynh on 10/2/17.
//

class MockDinnerTonightStoreService(private val context: Context) : StoreService {
    override fun getStore(storeId: String): Promise<Store> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getStores(latitude: Double, longitude: Double, radius: Double?): Promise<StoreSearchResult> {
        return Promise(Unit)
                .then(on = PromiseDispatch.BACKGROUND.executor) {
                    val stream = this.context.assets.open("store-response.json")
                    val result = JSONDecoder().decode(type = StoreSearchResult::class.java, value = stream.readBytes())
                    if (result == null) throw IllegalArgumentException()
                    result!!
                }
    }

}
