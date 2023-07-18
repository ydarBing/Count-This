package com.gurpgork.countthis.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.gurpgork.countthis.core.database.model.CtLocationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {

    @Transaction
    suspend fun updateLocation(location: CtLocationEntity) {
        location.let {
            deleteLocations()
            insertLocation(it)
        }
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLocation(location: CtLocationEntity)

    @Query("DELETE FROM location_table")
    suspend fun deleteLocations()

    @Query("SELECT * FROM location_table ORDER BY time")
    fun getLocations(): Flow<List<CtLocationEntity>>

    @Query("SELECT * FROM location_table ORDER BY time LIMIT 1")
    fun getMostRecentLocationEntity(): Flow<CtLocationEntity?>
}