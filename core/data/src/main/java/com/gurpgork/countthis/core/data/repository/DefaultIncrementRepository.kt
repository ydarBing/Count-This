package com.gurpgork.countthis.core.data.repository

import com.gurpgork.countthis.core.database.dao.CounterDao
import com.gurpgork.countthis.core.database.dao.IncrementDao
import com.gurpgork.countthis.core.database.model.IncrementEntity
import com.gurpgork.countthis.core.database.model.asExternalModel
import com.gurpgork.countthis.core.model.data.CtLocation
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
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
        instantTime: Instant,
        isDecrement: Boolean,
    ) {
        val counter = counterDao.getCounterById(counterId)

        val trackLocation = counter?.trackLocation != null && counter.trackLocation == true
        val entry = IncrementEntity(
            counterId = counterId,
            incrementDate = instantTime,
            zoneId = ZoneId.systemDefault(),
            increment = if (isDecrement) -counter?.increment!! else counter?.increment,
            accuracy = if (trackLocation) location?.accuracy else null,
            latitude = if (trackLocation) location?.latitude else null,
            longitude = if (trackLocation) location?.longitude else null,
            altitude = if (trackLocation) location?.altitude else null,
        )
        // this doesn't add default values
        incrementDao.upsert(entry)
    }

    override suspend fun getIncrements(counterId: Long) =
        incrementDao.observeIncrementsFromCounterId(id = counterId)
            .map { it.map(IncrementEntity::asExternalModel) }


    override suspend fun deleteIncrements(incrementIds: Set<Long>) =
        incrementDao.deleteIncrement(incrementIds)
}