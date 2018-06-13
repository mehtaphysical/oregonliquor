package info.oregonliquor.store

import info.oregonliquor.liquor.LiquorSearch
import info.oregonliquor.oregonliquorsearch.OregonLiquorSearch
import org.jsoup.nodes.Document
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class StoreSearch {

    @Autowired
    private lateinit var storeService: StoreService

    @Autowired
    private lateinit var liquorSearch: LiquorSearch

    @Async
    fun search() {
        val cookie = OregonLiquorSearch.auth()
        val count = getCount(cookie)
        val doc = getPage(count, cookie)
        val stores = (1..count).map { getStore(doc, it) }.map { storeService.create(it) }

        stores.forEach { liquorSearch.search(it.id).get() }
    }

    private fun getCount(cookie: String): Int {
        val doc = OregonLiquorSearch.request("/servlet/FrontController?radiusSearchParam=1000&productSearchParam=&locationSearchParam=97229&btnSearch=Search&view=global&action=search",
                cookie)
        return doc.body()
                .getElementById("results-header")
                .getElementsByTag("p")[0]
                .getElementsByTag("strong")[1]
                .text().toInt()
    }

    private fun getPage(count: Int, cookie: String): Document {
        return OregonLiquorSearch.request("/servlet/FrontController?view=locationlist&action=pagechange&column=Distance&pageSize=$count", cookie)
    }

    private fun getStore(doc: Document, index: Int): Store {
        val storeRow = doc.body()
                .getElementsByClass("list")[0]
                .getElementsByTag("tr")[index]

        val id = storeRow.getElementsByTag("td")[0].text().toLong()
        val name = ""
        val location = storeRow.getElementsByTag("td")[1].text()
        val address = storeRow.getElementsByTag("td")[2].text()
        val zipcode = storeRow.getElementsByTag("td")[3].text()
        val phone = storeRow.getElementsByTag("td")[4].text()
        val hours = storeRow.getElementsByTag("td")[5].text()

        return Store(id = id, name = name, location = location, address = address, zipcode = zipcode, phone = phone, hours = hours, bottles = listOf())
    }
}
