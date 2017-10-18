package com.heb.dtn.service.domain.info

//
// Created by Khuong Huynh on 9/26/17.
//

class ServerStatus {
    var status: String? = null
}

fun ServerStatus.isUp(): Boolean = this.status == "UP"
