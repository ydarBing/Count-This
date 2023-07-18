package com.gurpgork.countthis.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gurpgork.countthis.core.database.dao.CounterDao
import com.gurpgork.countthis.core.database.dao.HistoryDao
import com.gurpgork.countthis.core.database.dao.IncrementDao
import com.gurpgork.countthis.core.database.dao.LocationDao
import com.gurpgork.countthis.core.database.model.CounterEntity
import com.gurpgork.countthis.core.database.model.CtLocationEntity
import com.gurpgork.countthis.core.database.model.HistoryEntity
import com.gurpgork.countthis.core.database.model.IncrementEntity
import com.gurpgork.countthis.core.database.util.CtTypeConverters

@Database(
    entities = [
        CounterEntity::class,
        HistoryEntity::class,
        IncrementEntity::class,
        CtLocationEntity::class,
    ],
    version = 1,
//    exportSchema = true
)
@TypeConverters(
    CtTypeConverters::class,
)
abstract class CtDatabase: RoomDatabase() {
    abstract fun counterDao(): CounterDao
    abstract fun historyDao(): HistoryDao
    abstract fun incrementDao(): IncrementDao
    abstract fun locationDao(): LocationDao
}
