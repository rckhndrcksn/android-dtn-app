package com.heb.dtn.service.domain.store

//
// Created by Khuong Huynh on 9/27/17.
//

class Store {
    companion object {
        // South Flores Market HEB store.
        val defaultStore: Store
            get() {
                val defaultCenterMapStore = Store()
                defaultCenterMapStore.storeNumber = 718
                defaultCenterMapStore.latitude = 29.4192143
                defaultCenterMapStore.longitude = -98.4966322
                return defaultCenterMapStore
            }
    }

    var storeNumber: Long = 0
    var name: String? = null
    var address: String? = null
    var address2: String? = null
    var city: String? = null
    var state: String? = null
    var latitude: Double? = null
    var longitude: Double? = null

    internal var ID: String? = null
    internal var zip: String? = null

    var zipcode: String?
        get() = this.zip
        set(value) { this.zip = value }
}

/* Extensions */
var Store.storeId: String?
    get() = this.ID
    set(value) { this.ID = value }

val Store.streetAddress2: String?
    get(){
        var str = ""

        this.city?.let {
            str += this.city
        }

        this.state?.let {
            if (str.isNotEmpty()) {
                str += ", "
            }

            str += this.state
        }

        this.zipcode?.let {
            if (str.isNotEmpty()) {
                str += ", "
            }

            str += this.zipcode
        }

        return str
    }

val Store.mailingAddress: String?
    get() = "${address ?: ""} ${address2 ?: ""}\n${city ?: ""} ${state ?: ""} ${zipcode ?: ""}"
