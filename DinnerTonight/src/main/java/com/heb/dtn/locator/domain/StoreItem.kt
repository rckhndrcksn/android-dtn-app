package com.heb.dtn.locator.domain

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.heb.dtn.extensions.metersToMiles
import com.heb.dtn.extensions.roundTo
import com.heb.dtn.service.domain.store.Store
import com.heb.dtn.service.domain.store.mailingAddress
import com.heb.dtn.service.domain.store.storeId
import com.heb.dtn.service.domain.store.streetAddress2
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable

class StoreItem(store: Store) : ClusterItem, Serializable {
    @Transient private var position: LatLng
    val name: String
    val storeId: String
    val address1: String
    val address2: String
    var distanceToLocation = 0.0
    //val pharmacyHours: String
    //val pharmacyPhone: String
    val mailingAddress: String

    init {
        this.position = LatLng(store.latitude ?: 0.0, store.longitude ?: 0.0)
        this.name = store.name ?: "Store #" + store.storeNumber
        this.storeId = store.storeId ?: "" + store.storeNumber
        this.address1 = store.address ?: "123 Main St.\nAustin, TX 78701"
        this.address2 = store.streetAddress2 ?: "123 Main St."
        //this.pharmacyHours = store.pharmacyHours ?: ""
        //this.pharmacyPhone = store.pharmacyPhone ?: ""
        this.mailingAddress = store.mailingAddress ?: "123 Main St.\nAustin, TX 78701"
    }

    override fun getPosition(): LatLng = position
    override fun getTitle(): String = ""
    override fun getSnippet(): String = ""
    override fun equals(other: Any?): Boolean = this.storeId == (other as StoreItem?)?.storeId
    override fun hashCode(): Int = this.storeId.hashCode()

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
