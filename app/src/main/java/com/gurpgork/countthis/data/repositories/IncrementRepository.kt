package com.gurpgork.countthis.data.repositories

import com.gurpgork.countthis.counter.IncrementEntity
import com.gurpgork.countthis.data.daos.CounterDao
import com.gurpgork.countthis.data.daos.IncrementDao
import com.gurpgork.countthis.location.CTLocation
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IncrementRepository @Inject constructor(
    private val counterDao: CounterDao,
    private val incrementDao: IncrementDao
) {
    suspend fun addIncrement(counterId: Long, ctLocation: CTLocation?, timestamp: OffsetDateTime, instantTime: Instant, isDecrement: Boolean = false){
        val counter = counterDao.getCounter(counterId)

        if(counter != null)
        {
            val trackLocation = counter.track_location != null && counter.track_location == true
            val entry = IncrementEntity(
                counterId = counterId,
                date = timestamp,
                // using default database value (uses seconds instead of Instant.now()'s nanosecond precision)
                // wanting to use epoch
                instantUTC = instantTime.toEpochMilli(),
                zoneOffset = timestamp.offset,
                zoneId = ZoneId.systemDefault(),
                increment = if(isDecrement) -counter.increment else counter.increment,
                accuracy = if(trackLocation) ctLocation?.accuracy else null,
                latitude = if(trackLocation) ctLocation?.latitude else null,
                longitude = if(trackLocation) ctLocation?.longitude else null,
                altitude = if(trackLocation) ctLocation?.altitude else null,
            )
            // this doesn't add default values
            incrementDao.insertOrUpdate(entry)
//            incrementDao.insertWithDefaultValues(
//                entry.counterId,
//                entry.date,
//                entry.zoneOffset ?: ZoneOffset.UTC,
//                entry.zoneId ?: ZoneId.systemDefault(),
//                entry.increment ?: 1)
        }
    }

    suspend fun getIncrements(counterId: Long) = incrementDao.observeIncrementsFromCounterId(id = counterId)

//    suspend fun save(increment: IncrementEntity) = incrementDao.insertOrUpdate(increment)
//    suspend fun save(increments: List<IncrementEntity>) = incrementDao.insertOrUpdate(increments)
}