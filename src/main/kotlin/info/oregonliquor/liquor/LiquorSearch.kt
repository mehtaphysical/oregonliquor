package info.oregonliquor.liquor

import info.oregonliquor.liquor.category.Category
import info.oregonliquor.liquor.bottle.LiquorBottle
import info.oregonliquor.oregonliquorsearch.OregonLiquorSearch
import info.oregonliquor.store.Store
import info.oregonliquor.store.StoreService
import org.jsoup.nodes.Document
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.util.concurrent.CompletableFuture


@Service
class LiquorSearch {

    private final val logger = LoggerFactory.getLogger(LiquorSearch::class.java)

    @Autowired
    private lateinit var liquorService: LiquorService

    @Autowired
    private lateinit var storeService: StoreService

    @Async
    fun search(storeId: Long): CompletableFuture<Unit> {
        val cookie = OregonLiquorSearch.auth()
        val count = getCount(storeId, cookie)
        val store = storeService.getById(storeId)
        if (store == null) {
            throw Exception("No store with id $storeId")
        }

        getLiquorIds(storeId, count, cookie).forEach { id ->
            try {
                saveLiquor(id, store, cookie)
            } catch (e: Exception) {
                logger.error("Unable to save liquor $id", e)
            }
        }

        return CompletableFuture.completedFuture(Unit)
    }

    private fun getCount(storeId: Long, cookie: String): Int {
        val doc = OregonLiquorSearch.request("/servlet/FrontController?radiusSearchParam=0&productSearchParam=&locationSearchParam=$storeId&btnSearch=Search&view=global&action=search",
                cookie)

        return doc.body().getElementById("results-header").getElementsByTag("strong")[1].text().toInt()
    }

    private fun getLiquorIds(storeId: Long, count: Int, cookie: String): List<String> {
        val doc = OregonLiquorSearch.request("/servlet/FrontController?view=locationdetails&agencyNumber=$storeId&action=pagechange&locationRowNum=1&column=Description&pageSize=$count", cookie)
        return doc.body().select(".list td:first-child").map {
            it.text()
        }
    }

    private fun saveLiquor(id: String, store: Store, cookie: String) {
        logger.info("Saving liquor $id")
        val doc = OregonLiquorSearch.request("/servlet/FrontController?view=locationdetails&agencyNumber=${store.id}&action=productselect&itemCode=$id&locationRowNum=1&productRowNum=1",
                cookie)

        val liquor = liquorService.create(getName(doc, id), getCategories(doc), listOf())

        val bottle = LiquorBottle(id = id, size = getSize(doc), storeBottles = listOf(), liquor = liquor)
        liquorService.addBottle(liquor, bottle)
        storeService.addBottle(store, bottle, getPrice(doc), getQuantity(doc))
    }

    private fun getName(doc: Document, id: String): String {
        return doc.body()
                .getElementById("product-desc")
                .getElementsByTag("h2")
                .first()
                .text()
                .replace("Item $id: ", "")
    }

    private fun getCategories(doc: Document): List<Category> {
        return doc.body()
                .getElementById("product-details")
                .getElementsByTag("tr")[2]
                .getElementsByTag("td")[0]
                .text()
                .split("|")
                .map { Category(name = it) }
    }

    private fun getSize(doc: Document): String {
        return doc.body()
                .getElementById("product-details")
                .getElementsByTag("tr")[3]
                .getElementsByTag("td")[0]
                .text()
    }

    private fun getPrice(doc: Document): Float {
        return doc.body()
                .getElementById("product-details")
                .getElementsByTag("tr")[4]
                .getElementsByTag("td")[1]
                .text()
                .substring(1)
                .toFloat()
    }

    private fun getQuantity(doc: Document): Int {
        return try {
            doc.body()
                    .getElementById("in-stock")
                    .getElementsByTag("h2")[0]
                    .text()
                    .replace(" Bottle[s]? In Stock!".toRegex(), "")
                    .toInt()
        } catch(e: Exception) {
            0
        }
    }
}
