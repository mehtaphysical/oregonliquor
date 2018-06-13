package info.oregonliquor.store

import info.oregonliquor.liquor.bottle.LiquorBottle
import info.oregonliquor.liquor.bottle.LiquorBottleRepository
import info.oregonliquor.store.bottle.StoreBottle
import info.oregonliquor.store.bottle.StoreBottleRepository
import info.oregonliquor.store.price.StoreBottlePrice
import info.oregonliquor.store.price.StoreBottlePriceRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class StoreService {

    @Autowired
    private lateinit var storeRepository: StoreRepository

    @Autowired
    private lateinit var storeBottleRepository: StoreBottleRepository

    @Autowired
    private lateinit var storeBottlePriceRepository: StoreBottlePriceRepository

    @Autowired
    lateinit var liquorBottleRepository: LiquorBottleRepository

    @Transactional
    fun create(store: Store): Store {
        return storeRepository.save(store)
    }

    fun getById(id: Long): Store? {
        return storeRepository.findById(id).orElse(null)
    }

    fun getAll(): List<Store> {
        return storeRepository.findAll().toList()
    }

    fun addBottle(store: Store, bottle: LiquorBottle, price: Float, quantity: Int) {
        val storeBottle = storeBottleRepository.save(StoreBottle(liquorBottle = bottle, store = store, quantity = quantity))
        storeBottlePriceRepository.save(StoreBottlePrice(price = price, storeBottle = storeBottle, createdAt = LocalDateTime.now()))
        storeRepository.updateIdWithBottle(store.id, storeBottle.id)
        liquorBottleRepository.updateIdWithBottle(bottle.id, storeBottle.id)
    }

}
