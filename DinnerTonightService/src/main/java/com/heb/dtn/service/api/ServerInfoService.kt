package com.heb.dtn.service.api

import com.heb.dtn.service.domain.ServerStatus
import com.inmotionsoftware.promise.Promise

//
// Created by Khuong Huynh on 9/22/17.
//

interface ServerInfoService {

    fun serverStatus(): Promise<ServerStatus>

    fun isServerHealthy(): Promise<Boolean>

}
