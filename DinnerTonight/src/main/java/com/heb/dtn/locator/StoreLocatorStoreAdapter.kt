package com.heb.dtn.locator

//
// Created by Khuong Huynh on 12/2/16.
//

import com.heb.dtn.locator.domain.StoreItem

internal interface StoreLocatorStoreAdapter {

    fun setStoreItems(stores: List<StoreItem>?)

}
