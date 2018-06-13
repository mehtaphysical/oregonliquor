package info.oregonliquor.liquor.bottle

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import info.oregonliquor.liquor.Liquor
import info.oregonliquor.store.bottle.StoreBottle
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.OneToMany

@Entity
data class LiquorBottle(
        @Id val id: String = "",
        val size: String,
        @ManyToOne @JsonIgnoreProperties("bottles") val liquor: Liquor,
        @OneToMany @JsonIgnoreProperties("liquorBottle") var storeBottles: List<StoreBottle>) {
    constructor() : this(id = "", size = "", liquor = Liquor(), storeBottles = listOf())
}
