package info.oregonliquor.store

import info.oregonliquor.liquor.category.Category
import info.oregonliquor.store.bottle.StoreBottle
import info.oregonliquor.store.price.StoreBottlePriceRepository
import javassist.NotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class StoreJsonModels {

    @Autowired
    private lateinit var storeService: StoreService

    @Autowired
    private lateinit var storeBottlePriceRepository: StoreBottlePriceRepository

    fun store(id: Long): StoreJson {
        val s = storeService.getById(id)
        if (s == null) {
            throw NotFoundException("Store with $id not found")
        }
        val bottles = s.bottles.map { toBottleJson(it) }

        return StoreJson(id = s.id,
                name = s.name,
                location = s.location,
                address = s.address,
                zipcode = s.zipcode,
                phone = s.phone,
                hours = s.hours,
                bottles = bottles)
    }

    fun stores() : List<StoreJsonMinimal> {
        return storeService.getAll().map {
            StoreJsonMinimal(id = it.id,
                    name = it.name,
                    location = it.location,
                    address = it.address,
                    zipcode = it.zipcode,
                    phone = it.phone,
                    hours = it.hours)
        }
    }

    private fun toBottleJson(bottle: StoreBottle): BottleJson {
        return BottleJson(id = bottle.id,
                name = bottle.liquorBottle.liquor.name,
                categories = bottle.liquorBottle.liquor.categories.map { it.name },
                size = bottle.liquorBottle.size,
                quantity = bottle.quantity,
                price = storeBottlePriceRepository.findFirstByStoreBottleIdOrderByCreatedAtDesc(bottle.id).price)
    }
}

data class StoreJsonMinimal(val id: Long,
                            val name: String,
                            val location: String,
                            val address: String,
                            val zipcode: String,
                            val phone: String,
                            val hours: String)

data class StoreJson(val id: Long,
                     val name: String,
                     val location: String,
                     val address: String,
                     val zipcode: String,
                     val phone: String,
                     val hours: String,
                     val bottles: List<BottleJson>)

data class BottleJson(val id: Long,
                      val name: String,
                      val categories: List<String>,
                      val size: String,
                      val quantity: Int,
                      val price: Float)
