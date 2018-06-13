package info.oregonliquor.store.price

import org.springframework.data.repository.CrudRepository

interface StoreBottlePriceRepository : CrudRepository<StoreBottlePrice, Long> {
    fun findFirstByStoreBottleIdOrderByCreatedAtDesc(storeBottleId: Long): StoreBottlePrice
}
