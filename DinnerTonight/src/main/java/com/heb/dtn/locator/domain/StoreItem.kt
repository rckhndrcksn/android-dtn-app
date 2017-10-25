package com.heb.dtn.locator.domain

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.heb.dtn.extensions.metersToMiles
import com.heb.dtn.extensions.roundTo
import com.heb.dtn.service.domain.store.*
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable

class StoreItem(store: Store) : ClusterItem, Serializable {
    @Transient private var position: LatLng
    val name: String
    private val storeNumber: StoreNumber
    val address1: String
    val address2: String
    var distanceToLocation = 0.0
    val storePhone: String

    init {
        this.position = LatLng(store.latitude, store.longitude)
        this.name = store.name
        this.storeNumber = store.storeNumber
        this.address1 = store.address
        this.address2 = store.cityStateZip
        this.storePhone = store.phone
    }

    override fun getPosition(): LatLng = position
    override fun getTitle(): String = ""
    override fun getSnippet(): String = ""
    override fun equals(other: Any?): Boolean = this.storeNumber == (other as StoreItem?)?.storeNumber
    override fun hashCode(): Int = this.storeNumber.hashCode()

    fun setDistanceToLocation(location: Location) {
        val fromLocation = Location("")
        fromLocation.latitude = this.position.latitude
        fromLocation.longitude = this.position.longitude

        this.distanceToLocation = fromLocation.distanceTo(location).metersToMiles().roundTo(2)
    }

    @Throws(IOException::class)
    private fun writeObject(out: ObjectOutputStream) {
        out.defaultWriteObject()
        out.writeDouble(this.position.latitude)
        out.writeDouble(this.position.longitude)
    }

    @Throws(IOException::class, ClassNotFoundException::class)
    private fun readObject(input: ObjectInputStream) {
        input.defaultReadObject()
        this.position = LatLng(input.readDouble(), input.readDouble())
    }
}
