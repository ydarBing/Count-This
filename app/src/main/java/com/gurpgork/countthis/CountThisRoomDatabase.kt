package com.gurpgork.countthis

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gurpgork.countthis.counter.HistoryEntity
import com.gurpgork.countthis.counter.IncrementEntity
import com.gurpgork.countthis.data.CountThisDatabase
import com.gurpgork.countthis.data.entities.CounterEntity
import com.gurpgork.countthis.location.CTLocation

@Database(
    entities = [
        CounterEntity::class,
        HistoryEntity::class,
        IncrementEntity::class,
        CTLocation::class,
    ],
    version = 1,
//    exportSchema = false
)
@TypeConverters(CountThisTypeConverters::class)
abstract class CountThisRoomDatabase: RoomDatabase(), CountThisDatabase
