package com.heb.dtn.service.api

import com.inmotionsoftware.promise.Promise

//
// Created by Khuong Huynh on 9/22/17.
//

interface ServerInfoService {

    fun isServerHealthy(): Promise<Boolean>

}
