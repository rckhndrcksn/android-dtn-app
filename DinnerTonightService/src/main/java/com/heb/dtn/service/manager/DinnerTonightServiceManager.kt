package com.heb.dtn.service.manager

import com.heb.dtn.service.api.ServerInfoService
import java.net.URL

//
// Created by Khuong Huynh on 9/22/17.
//

enum class DinnerTonightServiceEnvironment {
    Dev
    , Staging
    , Release
    ;

    fun baseUrl(): URL =
        when(this) {
            Staging -> URL("http://35.202.145.21/")
            else -> URL("http://35.202.145.21/")
        }
}

interface DinnerTonightServiceManager {

    fun serverInfoService(): ServerInfoService

}
