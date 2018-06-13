package info.oregonliquor.store

import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.transaction.annotation.Transactional

interface StoreRepository : CrudRepository<Store, Long> {
    @Modifying
    @Transactional
    @Query("""
        INSERT INTO store_bottles (store_id, bottles_id) VALUES (?1, ?2) ON CONFLICT DO NOTHING
        """, nativeQuery = true)
    fun updateIdWithBottle(id: Long, bottleId: Long)
}
