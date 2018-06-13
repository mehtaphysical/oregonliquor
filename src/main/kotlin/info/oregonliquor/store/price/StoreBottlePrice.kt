package info.oregonliquor.store.price

import info.oregonliquor.store.bottle.StoreBottle
import java.time.LocalDateTime
import javax.persistence.*

@Entity
data class StoreBottlePrice(
        @Id @GeneratedValue(strategy = GenerationType.AUTO) val id: Long = 0,
        val price: Float,
        @ManyToOne val storeBottle: StoreBottle,
        val createdAt: LocalDateTime
) {
    private constructor() : this(id = 0, price = 0f, storeBottle = StoreBottle(), createdAt = LocalDateTime.now())
}
