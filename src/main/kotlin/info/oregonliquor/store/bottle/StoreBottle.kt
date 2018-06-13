package info.oregonliquor.store.bottle

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import info.oregonliquor.liquor.bottle.LiquorBottle
import info.oregonliquor.store.Store
import javax.persistence.*

@Entity
data class StoreBottle(
        @Id @GeneratedValue(strategy = GenerationType.AUTO) val id: Long = 0,
        @ManyToOne @JsonIgnoreProperties("storeBottles") val liquorBottle: LiquorBottle,
        @ManyToOne @JsonIgnoreProperties("bottles") val store: Store,
        val quantity: Int) {
    constructor() : this(id = 0, liquorBottle = LiquorBottle(), store = Store(), quantity = 0)
    constructor(store: Store, quantity: Int) : this(id = 0, liquorBottle = LiquorBottle(), store = store, quantity = quantity)
}
