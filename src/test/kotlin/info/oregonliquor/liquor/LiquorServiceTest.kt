package info.oregonliquor.liquor

import info.oregonliquor.liquor.category.Category
import info.oregonliquor.liquor.bottle.LiquorBottle
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
internal class LiquorServiceTest {

    @Autowired
    private lateinit var liquorService: LiquorService

    @Test
    fun create() {
        val liquor = Liquor(name = "Ardbeg 10", categories = listOf(), bottles = listOf())
        val created = liquorService.create(liquor)

        assertThat(created.name).isEqualTo(liquor.name)
        assertThat(created.categories).isEmpty()
        assertThat(created.bottles).isEmpty()
    }

    @Test
    fun createExisting() {
        liquorService.create(Liquor(name = "Ardbeg 10", categories = listOf(), bottles = listOf()))
        liquorService.create(Liquor(name = "Ardbeg 10", categories = listOf(), bottles = listOf()))
        assertThat(liquorService.getAll().size).isEqualTo(1)
    }

    @Test
    fun createWithCategories() {
        val liquor = Liquor(name = "Ardbeg 10", categories = listOf(Category(name = "SCOTCH")), bottles = listOf())
        val created = liquorService.create(liquor)

        assertThat(created.categories.size).isEqualTo(1)
        assertThat(created.categories[0].name).isEqualTo("SCOTCH")
    }

    @Test
    fun create1() {
        val created = liquorService.create("Ardbeg 10", listOf(Category(name = "SCOTCH")), listOf())
        assertThat(created.name).isEqualTo("Ardbeg 10")
        assertThat(created.categories.first().name).isEqualTo("SCOTCH")
    }

    @Test
    fun addBottle() {
    }

    @Test
    fun addBottles() {
        val l = liquorService.create(Liquor(name = "Ardbeg 10", categories = listOf(), bottles = listOf()))
        liquorService.addBottles(l, listOf(LiquorBottle()))

        val liquor = liquorService.getAll().first()
        assertThat(liquor.bottles.size).isEqualTo(1)
    }

    @Test
    fun getForCategory() {
    }
}
