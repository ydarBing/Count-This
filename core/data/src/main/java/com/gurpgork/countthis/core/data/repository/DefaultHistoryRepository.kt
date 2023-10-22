package com.gurpgork.countthis.core.data.repository

import com.gurpgork.countthis.core.database.dao.CounterDao
import com.gurpgork.countthis.core.database.dao.HistoryDao
import com.gurpgork.countthis.core.database.model.HistoryEntity
import com.gurpgork.countthis.core.database.model.asExternalModel
import com.gurpgork.countthis.core.model.data.CtLocation
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultHistoryRepository @Inject constructor(
    private val counterDao: CounterDao,
    private val historyDao: HistoryDao
) : HistoryRepository {
    override suspend fun addHistory(
        counterId: Long,
        location: CtLocation?,
        instantTime: Instant,
        isDecrement: Boolean,
    ) {
        val counter = counterDao.getCounterById(counterId)
        val lastReset = historyDao.getMostRecentReset(counterId)

        if (counter != null) {
            historyDao.insert(
                HistoryEntity(
                    counterId = counterId,
                    count = counter.count,
                    startDate = lastReset?.endDate ?: counter.creationDate,
                    endDate = Clock.System.now(),
                )
            )
        }
    }

    override suspend fun getHistory(counterId: Long) =
        historyDao.observeHistory(id = counterId)
            .map { it.map(HistoryEntity::asExternalModel) }


    override suspend fun deleteHistory(historyIds: Set<Long>) =
        historyDao.deleteHistory(historyIds)
}