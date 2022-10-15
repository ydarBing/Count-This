package com.gurpgork.countthis.data.daos

import androidx.room.*
import com.gurpgork.countthis.counter.HistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class  HistoryDao : EntityDao<HistoryEntity>(){

    @Query("SELECT * FROM history WHERE id IN (:id)")
    abstract fun observeHistory(id: Long): Flow<List<HistoryEntity>>

    @Query("SELECT * FROM history WHERE id IN (:id)")
    abstract suspend fun getHistory(id: Long): List<HistoryEntity>

    @Query("SELECT * FROM history WHERE id IN (:id) ORDER BY end_date DESC LIMIT 1")
    abstract suspend fun getMostRecentReset(id: Long): HistoryEntity?

    @Update
    abstract suspend fun updateCounter(counter: HistoryEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertHistory(history: HistoryEntity)

    @Delete
    abstract suspend fun deleteHistory(history: HistoryEntity)

    @Query("DELETE FROM history")
    abstract suspend fun deleteAll()
}