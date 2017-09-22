package com.heb.dtn.service.manager

import android.content.Context
import com.heb.dtn.foundation.service.HTTPService
import com.heb.dtn.service.api.ServerInfoService
import com.heb.dtn.service.internal.dtn.DinnerTonightServerInfoService

//
// Created by Khuong Huynh on 9/22/17.
//

class DefaultDinnerTonightServiceManager(private val context: Context, private val environment: DinnerTonightServiceEnvironment) : DinnerTonightServiceManager {

    private val config: HTTPService.Config = HTTPService.Config(baseUrl = environment.baseUrl())

    override fun serverInfoService(): ServerInfoService = DinnerTonightServerInfoService(config = this.config)

}
