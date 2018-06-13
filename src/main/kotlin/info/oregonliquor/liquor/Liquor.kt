package info.oregonliquor.liquor

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import info.oregonliquor.liquor.category.Category
import info.oregonliquor.liquor.bottle.LiquorBottle
import javax.persistence.*

@Entity
data class Liquor(
        @Id @GeneratedValue(strategy = GenerationType.AUTO) val id: Long = 0,
        @Column(unique = true) val name: String,
        @ManyToMany @JsonIgnoreProperties("liquors") val categories: List<Category>,
        @OneToMany @JsonIgnoreProperties("liquor") var bottles: List<LiquorBottle>
) {
    constructor() : this(id = 0, name = "", categories = listOf(), bottles = listOf())
}
