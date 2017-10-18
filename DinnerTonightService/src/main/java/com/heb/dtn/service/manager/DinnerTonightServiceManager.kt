package com.heb.dtn.service.manager

import com.heb.dtn.service.api.*
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
            Dev -> URL("https://dinnertonight.dev.heb-commerce-platform.com/")
            Staging -> URL("https://dinnertonight.stage.heb-commerce-platform.com/")
            else -> URL("https://dinnertonight.heb-commerce-platform.com/")
        }
}

interface DinnerTonightServiceManager : DinnerTonightOAuthServiceManager {

    fun serverInfoService(): ServerInfoService

    fun productService(): ProductService

    fun storeService(): StoreService

    fun cartService(): CartService

    fun orderService(): OrderService

}
