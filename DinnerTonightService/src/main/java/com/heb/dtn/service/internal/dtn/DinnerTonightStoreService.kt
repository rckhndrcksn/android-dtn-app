package com.heb.dtn.service.internal.dtn

import android.content.Context
import com.heb.dtn.foundation.service.JSONDecoder
import com.heb.dtn.service.api.StoreService
import com.heb.dtn.service.domain.StoreResults

/**
 * Created by jcarbo on 9/29/17.
 */
class DinnerTonightStoreService : StoreService {
    var context: Context? = null

    constructor(context: Context) {
        this.context = context
    }

    override fun locateStore(lat: Double, lon: Double, radius: Int): StoreResults {
        var returnValue: StoreResults? = null
        val stream = context?.assets?.open("store-response.json")
        val decoder = JSONDecoder()
        decoder.let {
            returnValue = it.decode(type = StoreResults::class.java, value = stream!!.readBytes())
        }

        return returnValue!!
    }
}