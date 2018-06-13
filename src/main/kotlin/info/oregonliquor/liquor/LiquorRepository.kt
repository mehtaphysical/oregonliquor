package info.oregonliquor.liquor

import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.transaction.annotation.Transactional

interface LiquorRepository : CrudRepository<Liquor, Long> {
    fun findByName(name: String): Liquor?

    @Modifying
    @Transactional
    @Query("""
        INSERT INTO liquor_bottles (liquor_id, bottles_id) VALUES (?1, ?2) ON CONFLICT DO NOTHING
        """, nativeQuery = true)
    fun updateIdWithBottle(id: Long, bottleId: String)
}
