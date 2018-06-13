package info.oregonliquor.liquor

import info.oregonliquor.liquor.category.Category
import info.oregonliquor.liquor.category.CategoryService
import info.oregonliquor.liquor.bottle.LiquorBottle
import info.oregonliquor.liquor.bottle.LiquorBottleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class LiquorService {

    @Autowired
    private lateinit var liquorRepository: LiquorRepository

    @Autowired
    private lateinit var bottleRepository: LiquorBottleRepository

    @Autowired
    private lateinit var categoryService: CategoryService

    @Transactional
    fun create(name: String, categories: List<Category>, bottles: List<LiquorBottle> = listOf()): Liquor {
        val liquor = Liquor(name = name, categories = categories, bottles = bottles)
        return create(liquor)
    }

    @Transactional
    fun create(liquor: Liquor): Liquor {
        val existingLiquor = liquorRepository.findByName(liquor.name)

        val createdCategories = liquor.categories.map { categoryService.create(it.name) }
        liquor.bottles.forEach { bottleRepository.save(it) }

        val storableLiquor = if (existingLiquor == null) {
            Liquor(
                    name = liquor.name,
                    categories = createdCategories,
                    bottles = liquor.bottles)
        } else {
            Liquor(
                    id = existingLiquor.id,
                    name = existingLiquor.name,
                    categories = existingLiquor.categories.union(createdCategories).toList(),
                    bottles = existingLiquor.bottles.union(liquor.bottles).toList()
            )
        }

        return liquorRepository.save(storableLiquor)
    }

    fun addBottle(liquor: Liquor, bottle: LiquorBottle): Liquor {
        return addBottles(liquor, listOf(bottle))
    }

    fun addBottles(liquor: Liquor, bottles: List<LiquorBottle>): Liquor {
        bottles
                .map { bottleRepository.save(it) }
                .forEach { liquorRepository.updateIdWithBottle(liquor.id, it.id) }
        return liquorRepository.findById(liquor.id).get()
    }

    fun getAll(): List<Liquor> {
        return liquorRepository.findAll().toList()
    }
}
