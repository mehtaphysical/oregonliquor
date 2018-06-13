package info.oregonliquor.liquor.bottle

import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.transaction.annotation.Transactional

interface LiquorBottleRepository : CrudRepository<LiquorBottle, String> {
    @Modifying
    @Transactional
    @Query("""
        INSERT INTO liquor_bottle_store_bottles (liquor_bottle_id, store_bottles_id) VALUES (?1, ?2) ON CONFLICT DO NOTHING
        """, nativeQuery = true)
    fun updateIdWithBottle(id: String, bottleId: Long)
}
