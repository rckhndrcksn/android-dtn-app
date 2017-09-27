package com.heb.dtn.service.internal.dtn

import com.heb.dtn.foundation.promise.android.then
import com.heb.dtn.foundation.service.HTTPService
import com.heb.dtn.foundation.service.get
import com.heb.dtn.service.api.ServerInfoService
import com.heb.dtn.service.domain.ServerStatus
import com.heb.dtn.service.domain.isUp
import com.inmotionsoftware.promise.Promise

//
// Created by Khuong Huynh on 9/22/17.
//

internal class DinnerTonightServerInfoService(config: HTTPService.Config) : HTTPService(config = config), ServerInfoService {

    override fun serverStatus(): Promise<ServerStatus> {
        val route = "api/v1/health"
        return this.get(route = route, type = ServerStatus::class.java)
    }

    override fun isServerHealthy(): Promise<Boolean>
        = this.serverStatus().then { it.isUp()  }

}
