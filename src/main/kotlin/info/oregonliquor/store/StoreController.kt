package info.oregonliquor.store

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class StoreController {

    @Autowired
    private lateinit var storeSearch: StoreSearch

    @Autowired
    private lateinit var storeJsonModels: StoreJsonModels

    @RequestMapping("/store/search")
    fun search() {
        storeSearch.search()
    }

    @RequestMapping("/store/{id}")
    fun store(@PathVariable("id") id: Long): StoreJson {
        return storeJsonModels.store(id)
    }

    @RequestMapping("/stores")
    fun stores(): List<StoreJsonMinimal> {
        return storeJsonModels.stores()
    }
}
