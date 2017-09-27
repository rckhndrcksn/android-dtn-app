package com.heb.dtn.service.internal.dtn

import com.heb.dtn.foundation.service.HTTPService
import com.heb.dtn.foundation.service.Parameters
import com.heb.dtn.foundation.service.get
import com.heb.dtn.service.api.StoreService
import com.heb.dtn.service.domain.store.Store
import com.heb.dtn.service.domain.store.StoreSearchResult
import com.inmotionsoftware.promise.Promise

/**
 * Created by jcarbo on 9/29/17.
 */

class DinnerTonightStoreService(config: HTTPService.Config) : HTTPService(config = config), StoreService {
    override fun getStore(storeId: String): Promise<Store> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getStores(latitude: Double, longitude: Double, radius: Double?): Promise<StoreSearchResult> {
        val route = "api/v1/locate/distance"
        val params: Parameters = mapOf("lat" to latitude.toString()
                                        , "lon" to longitude.toString()
                                        , "miles" to (radius?.toString() ?: "5.0"))

        return this.get(route = route, query = params, type = StoreSearchResult::class.java)
    }

//    var context: Context? = null
//
//    constructor(context: Context) {
//        this.context = context
//    }

//    override fun locateStore(lat: Double, lon: Double, radius: Int): StoreResults {
//        var returnValue: StoreResults? = null
//        val stream = context?.assets?.open("store-response.json")
//        val decoder = JSONDecoder()
//        decoder.let {
//            returnValue = it.decode(type = StoreResults::class.java, value = stream!!.readBytes())
//        }
//
//        return returnValue!!
//    }

}
