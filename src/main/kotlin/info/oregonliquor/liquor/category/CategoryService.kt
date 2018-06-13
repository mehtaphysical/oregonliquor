package info.oregonliquor.liquor.category

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CategoryService {

    @Autowired
    private lateinit var categoryRepository: CategoryRepository

    fun create(name: String) : Category {
        val category = categoryRepository.findByName(name)
        if (category != null) {
            return category
        }
        return categoryRepository.save(Category(name = name))
    }

    fun getCategoryByName(name: String): Category? {
        return categoryRepository.findByName(name)
    }
}
