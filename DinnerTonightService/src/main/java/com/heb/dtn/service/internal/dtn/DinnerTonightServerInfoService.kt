package com.heb.dtn.service.internal.dtn

import com.heb.dtn.foundation.promise.android.then
import com.heb.dtn.foundation.service.HTTPService
import com.heb.dtn.foundation.service.get
import com.heb.dtn.service.api.ServerInfoService
import com.inmotionsoftware.promise.Promise

//
// Created by Khuong Huynh on 9/22/17.
//

internal class DinnerTonightServerInfoService(config: HTTPService.Config) : HTTPService(config = config), ServerInfoService {

    override fun isServerHealthy(): Promise<Boolean> {
        val route = "api/v1/health"
        return this.get(route = route).then { true }
    }

}
