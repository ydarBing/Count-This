package com.gurpgork.countthis.core.data.repository

import com.gurpgork.countthis.core.database.dao.CounterDao
import com.gurpgork.countthis.core.database.dao.IncrementDao
import com.gurpgork.countthis.core.database.model.IncrementEntity
import com.gurpgork.countthis.core.database.model.asExternalModel
import com.gurpgork.countthis.core.model.data.CtLocation
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultIncrementRepository @Inject constructor(
    private val counterDao: CounterDao,
    private val incrementDao: IncrementDao
) : IncrementRepository {
    override suspend fun addIncrement(
        counterId: Long,
        location: CtLocation?,
        timestamp: OffsetDateTime,
        instantTime: Instant,
        isDecrement: Boolean,
    ) {
        val counter = counterDao.getCounter(counterId)

        val trackLocation = counter.trackLocation != null && counter.trackLocation == true
        val entry = IncrementEntity(
            counterId = counterId,
            date = timestamp,
            // using default database value (uses seconds instead of Instant.now()'s nanosecond precision)
            // wanting to use epoch
            instantUTC = instantTime.toEpochMilli(),
            zoneOffset = timestamp.offset,
            //TODO
//                fixedOffsetTimeZone = FixedOffsetTimeZone(UtcOffset.ZERO),

            zoneId = ZoneId.systemDefault(),
            increment = if (isDecrement) -counter.increment else counter.increment,
            accuracy = if (trackLocation) location?.accuracy else null,
            latitude = if (trackLocation) location?.latitude else null,
            longitude = if (trackLocation) location?.longitude else null,
            altitude = if (trackLocation) location?.altitude else null,
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

    override suspend fun getIncrements(counterId: Long) =
        incrementDao.observeIncrementsFromCounterId(id = counterId)
            .map { it.map(IncrementEntity::asExternalModel) }

//    suspend fun save(increment: IncrementEntity) = incrementDao.insertOrUpdate(increment)
//    suspend fun save(increments: List<IncrementEntity>) = incrementDao.insertOrUpdate(increments)
}