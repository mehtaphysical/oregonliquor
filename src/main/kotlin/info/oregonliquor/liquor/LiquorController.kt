package info.oregonliquor.liquor

import info.oregonliquor.liquor.category.Category
import info.oregonliquor.liquor.category.CategoryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class LiquorController {

    @Autowired
    private lateinit var liquorSearch: LiquorSearch

    @Autowired
    private lateinit var categoryService: CategoryService

    @Autowired
    private lateinit var liquorService: LiquorService

    @RequestMapping("/search")
    fun search() {
        liquorSearch.search(1181)
    }

    @RequestMapping("/liquor/category/{name}")
    fun liquorsForCategory(@PathVariable("name") name: String): Category {
        val category = categoryService.getCategoryByName(name)
        if (category == null) {
            throw Exception("No category by name: $name")
        }

        return category
    }
}
