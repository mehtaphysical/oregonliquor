package info.oregonliquor.store.bottle

import info.oregonliquor.store.price.StoreBottlePriceRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class StoreBottleService {

    @Autowired
    private lateinit var storeBottlePriceRepository: StoreBottlePriceRepository

    fun getLatestPrice(bottle: StoreBottle): Float {
        return storeBottlePriceRepository.findFirstByStoreBottleIdOrderByCreatedAtDesc(bottle.id).price
    }
}
