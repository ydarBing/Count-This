package com.gurpgork.countthis.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.gurpgork.countthis.core.database.model.IncrementEntity
import kotlinx.coroutines.flow.Flow
import java.time.ZoneId

@Dao
interface IncrementDao {//: EntityDao<IncrementEntity>(){

    @Query("SELECT * FROM increments WHERE id IN (:id)")
    fun observeIncrementsFromCounterId(id: Long): Flow<List<IncrementEntity>>


    @Query("SELECT * FROM increments WHERE id IN (:id)")
    fun getIncrementsFromCounterId(id: Long): List<IncrementEntity>

    @Query("INSERT INTO increments (counter_id, time_zone_id, increment) " +
            "VALUES(:counterId, :zoneId, :increment);")
    fun insertWithDefaultValues(
        counterId: Long,
        zoneId: ZoneId,
        increment: Int
    ): Long

//    @Query("INSERT INTO increments (counter_id, date, increment) VALUES(:entity.counterId,:date, :increment);")
//    fun insertWithDefaultValues(entity: IncrementEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: IncrementEntity) : Long

    suspend fun insertOrUpdate(entity: IncrementEntity): Long {
        return if (entity.id == 0L) {
            insert(entity)
        } else {
            update(entity)
            entity.id
        }
    }
    @Upsert
    suspend fun upsert(entity: IncrementEntity): Long

    @Update
    suspend fun update(entity: IncrementEntity)

    @Delete
    suspend fun deleteIncrement(increment: IncrementEntity)

    @Query("DELETE FROM increments WHERE counter_id = :counterId")
    suspend fun deleteAllFromCounterId(counterId: Long)

    @Query("DELETE FROM increments")
    suspend fun deleteAll()
}