package info.oregonliquor.store

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import info.oregonliquor.store.bottle.StoreBottle
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.ManyToMany

@Entity
data class Store(
        @Id val id: Long = 0,
        val name: String,
        val location: String,
        val address: String,
        val zipcode: String,
        val phone: String,
        val hours: String,
        @ManyToMany @JsonIgnoreProperties("store") var bottles: List<StoreBottle>
) {
    constructor() : this(id = 0, name = "", location = "", address = "", zipcode = "", phone = "", hours = "", bottles = listOf())
}
