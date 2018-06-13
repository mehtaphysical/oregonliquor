package info.oregonliquor.liquor.category

import org.springframework.data.repository.CrudRepository

interface CategoryRepository : CrudRepository<Category, Long> {
    fun findByName(name: String): Category?
}
