package com.gurpgork.countthis.data.daos

import androidx.room.*
import com.gurpgork.countthis.counter.IncrementEntity
import kotlinx.coroutines.flow.Flow
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset

@Dao
abstract class IncrementDao : EntityDao<IncrementEntity>(){

    @Query("SELECT * FROM increments WHERE id IN (:id)")
    abstract fun observeIncrementsFromCounterId(id: Long): Flow<List<IncrementEntity>>


    @Query("SELECT * FROM increments WHERE id IN (:id)")
    abstract fun getIncrementsFromCounterId(id: Long): List<IncrementEntity>

    @Query("INSERT INTO increments (counter_id, date, time_zone_offset, time_zone_id, increment) " +
            "VALUES(:counterId,:date, :zoneOffset, :zoneId, :increment);")
    abstract fun insertWithDefaultValues(
        counterId: Long,
        date: OffsetDateTime,
        zoneOffset: ZoneOffset,
        zoneId: ZoneId,
        increment: Int
    ): Long

//    @Query("INSERT INTO increments (counter_id, date, increment) VALUES(:entity.counterId,:date, :increment);")
//    abstract fun insertWithDefaultValues(entity: IncrementEntity): Long



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract override suspend fun insert(entity: IncrementEntity) : Long

    @Delete
    abstract suspend fun deleteIncrement(increment: IncrementEntity)

    @Query("DELETE FROM increments WHERE counter_id = :counterId")
    abstract suspend fun deleteAllFromCounterId(counterId: Long)

    @Query("DELETE FROM increments")
    abstract suspend fun deleteAll()
}