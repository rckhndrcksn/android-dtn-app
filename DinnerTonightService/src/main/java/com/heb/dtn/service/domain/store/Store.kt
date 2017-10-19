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

    var address: String
    var city: String
    var latitude: Double
    var longitude: Double
    var name: String
    var phone: String
    var state: String
    var storeNumber: Int
    var zip: String

    init {
        this.address = ""
        this.city = ""
        this.latitude = 0.0
        this.longitude = 0.0
        this.name = ""
        this.phone = ""
        this.state = ""
        this.storeNumber = 0
        this.zip = ""
    }
}

/* Extensions */
val Store.cityStateZip: String
    get(){
        // City
        var str = this.city

        // State
        if (str.isNotEmpty()) {
            str += ", "
        }
        str += this.state

        // Zipcode
        if (str.isNotEmpty()) {
            str += ", "
        }
        str += this.zip

        return str
    }
