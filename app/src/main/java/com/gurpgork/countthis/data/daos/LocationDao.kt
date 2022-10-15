package com.gurpgork.countthis.data.daos

import androidx.room.*
import com.gurpgork.countthis.location.Location
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {

    @Transaction
    suspend fun updateLocation(location: Location) {
        location.let {
            deleteLocations()
            insertLocation(it)
        }
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLocation(location: Location)

    @Query("DELETE FROM location_table")
    suspend fun deleteLocations()

    @Query("SELECT * FROM location_table ORDER BY time")
    fun getLocations(): Flow<List<Location>>

    @Query("SELECT * FROM location_table ORDER BY time LIMIT 1")
    fun getMostRecentLocation(): Flow<Location?>
}