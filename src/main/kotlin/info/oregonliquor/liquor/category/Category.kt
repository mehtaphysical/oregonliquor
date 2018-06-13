package info.oregonliquor.liquor.category

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import info.oregonliquor.liquor.Liquor
import javax.persistence.*

@Entity
data class Category(
        @Id @GeneratedValue(strategy = GenerationType.AUTO) val id: Long = 0,
        @Column(unique = true) val name: String,
        @ManyToMany(mappedBy = "categories")  @JsonIgnoreProperties("categories") val liquors: List<Liquor>
) {
    private constructor() : this(id = 0, name = "", liquors = listOf())
    constructor(name: String) : this(id = 0, name = name, liquors = listOf())
}
