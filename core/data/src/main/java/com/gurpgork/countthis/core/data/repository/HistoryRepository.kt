package com.gurpgork.countthis.core.data.repository

import com.gurpgork.countthis.core.model.data.CtLocation
import com.gurpgork.countthis.core.model.data.History
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant
import javax.inject.Singleton

@Singleton
interface HistoryRepository {
    suspend fun addHistory(
        counterId: Long,
        location: CtLocation?,
        instantTime: Instant,
        isDecrement: Boolean = false,
    )

    suspend fun getHistory(counterId: Long) : Flow<List<History>>
    suspend fun deleteHistory(historyIds: Set<Long>) : Int
}