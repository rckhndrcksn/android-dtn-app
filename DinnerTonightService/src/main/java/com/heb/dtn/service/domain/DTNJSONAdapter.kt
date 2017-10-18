package com.heb.dtn.service.domain

import com.heb.dtn.foundation.service.HTTPService
import com.heb.dtn.foundation.service.JSONAdapter
import com.heb.dtn.service.domain.account.Gigya
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonQualifier
import com.squareup.moshi.ToJson

//
// Created by Khuong Huynh on 10/19/17.
//

@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class HTTPServiceStatusCode

@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class GigyaErrorCode

internal class DTNJSONAdapter : JSONAdapter() {

    @FromJson
    @HTTPServiceStatusCode
    fun httpServiceStatusCodeFromJson(code: Int?): HTTPService.StatusCode?
        = code?.let { HTTPService.StatusCode.withValue(value = it)}

    @ToJson
    fun httpServiceStatusCodeToJson(@HTTPServiceStatusCode statusCode: HTTPService.StatusCode?): Int?
        = statusCode?.code

    @FromJson
    @GigyaErrorCode
    fun gigyaErrorCodeFromJson(code: Int?): Gigya.Error.Code?
        = code?.let { Gigya.Error.Code.withValue(value = it) }

    @ToJson
    fun gigyaErrorCodeToJson(@GigyaErrorCode errorCode: Gigya.Error.Code?): Int?
        = errorCode?.value
}
