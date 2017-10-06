package com.heb.dtn.service.manager

import com.heb.dtn.service.api.CartService
import com.heb.dtn.service.api.ProductService
import com.heb.dtn.service.api.ServerInfoService
import com.heb.dtn.service.api.StoreService
import java.net.URL

//
// Created by Khuong Huynh on 9/22/17.
//

enum class DinnerTonightServiceEnvironment {
    Dev
    , Staging
    , Release
    ;

    internal fun baseUrl(): URL =
        when(this) {
            Dev -> URL("http://35.202.209.115/")
            Staging -> URL("http://35.202.145.21/")
            else -> URL("http://35.202.145.21/")
        }
}

interface DinnerTonightServiceManager : DinnerTonightOAuthServiceManager {

    fun serverInfoService(): ServerInfoService

    fun productService(): ProductService

    fun storeService(): StoreService

    fun cartService(): CartService

}
