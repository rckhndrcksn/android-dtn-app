package com.heb.dtn.service.manager

import com.heb.dtn.service.api.*
import java.net.URL

//
// Created by Khuong Huynh on 9/22/17.
//

class GigyaServiceEnvironment(
    var baseUrl: URL,
    var apiKey: String,
    var userKey: String,
    var secret: String
)

enum class DinnerTonightServiceEnvironment {
    Dev
    , Staging
    , Release
    ;

    internal val baseUrl: URL get() =
        when(this) {
            Dev -> URL("https://dinnertonight.dev.heb-commerce-platform.com/")
            Staging -> URL("https://dinnertonight.stage.heb-commerce-platform.com/")
            else -> URL("https://dinnertonight.heb-commerce-platform.com/")
        }

    internal val gigya: GigyaServiceEnvironment get() =
        when(this) {
            Dev -> GigyaServiceEnvironment(
                baseUrl = URL("https://accounts.us1.gigya.com/"),
                apiKey = "3_ga0NNDHG5j07zMb3jB9GKEtHSfaSOyJnZPWy7Eui2sbVpbbY5j6iNi3aIZ0ARAcU",
                userKey = "APdklm61LI4x",
                secret = "l6RosOWitg7sxNTifmaf0dZL/rcLfOic"
            )
            Staging -> GigyaServiceEnvironment(
                baseUrl = URL("https://accounts.us1.gigya.com/"),
                apiKey = "3_ga0NNDHG5j07zMb3jB9GKEtHSfaSOyJnZPWy7Eui2sbVpbbY5j6iNi3aIZ0ARAcU",
                userKey = "APdklm61LI4x",
                secret = "l6RosOWitg7sxNTifmaf0dZL/rcLfOic"
            )
            Release -> GigyaServiceEnvironment(
                baseUrl = URL("https://accounts.us1.gigya.com/"),
                apiKey = "3_ga0NNDHG5j07zMb3jB9GKEtHSfaSOyJnZPWy7Eui2sbVpbbY5j6iNi3aIZ0ARAcU",
                userKey = "APdklm61LI4x",
                secret = "l6RosOWitg7sxNTifmaf0dZL/rcLfOic"
            )
        }
}

interface DinnerTonightServiceManager : DinnerTonightAuthServiceManager {

    fun serverInfoService(): ServerInfoService

    fun productService(): ProductService

    fun storeService(): StoreService

    fun cartService(): CartService

    fun orderService(): OrderService

}
