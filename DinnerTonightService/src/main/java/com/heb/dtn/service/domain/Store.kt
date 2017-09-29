package com.heb.dtn.service.domain

/**
 * Created by jcarbo on 9/29/17.
 */
class Store {
    var storeNumber: Long? = null
    var lat: Double? = null
    var lon: Double? = null

    override fun toString(): String {
        val sb = StringBuilder("")
        sb.append("storeNumber: ")
        if (storeNumber != null) {
            sb.append(storeNumber!!)
        }

        sb.append("\nlat: ")
        if (lat != null) {
            sb.append(lat!!)
        }

        sb.append("\nlon: ")
        if (lon != null) {
            sb.append(lon!!)
        }

        return sb.toString()
    }
}

class StoreResults {
    public var items: Array<Store>? = null
}