package com.gurpgork.countthis.core.database.dao

import androidx.room.*
import com.gurpgork.countthis.core.database.model.HistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {//: EntityDao<HistoryEntity>(){

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(history: HistoryEntity): Long

    suspend fun insertOrUpdate(entity: HistoryEntity): Long {
        return if (entity.id == 0L) {
            insert(entity)
        } else {
            update(entity)
            entity.id
        }
    }

    @Update
    suspend fun update(entity: HistoryEntity)

    @Query("SELECT * FROM history WHERE id IN (:id)")
    fun observeHistory(id: Long): Flow<List<HistoryEntity>>

    @Query("SELECT * FROM history WHERE id IN (:id)")
    suspend fun getHistory(id: Long): List<HistoryEntity>

    @Query("SELECT * FROM history WHERE id IN (:id) ORDER BY end_date DESC LIMIT 1")
    suspend fun getMostRecentReset(id: Long): HistoryEntity?

    @Update
    suspend fun updateCounter(counter: HistoryEntity)

    @Delete
    suspend fun deleteHistory(history: HistoryEntity)

    @Query("DELETE FROM history")
    suspend fun deleteAll()
}