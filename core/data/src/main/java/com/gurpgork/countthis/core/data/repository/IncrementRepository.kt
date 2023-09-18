package com.gurpgork.countthis.core.data.repository

import com.gurpgork.countthis.core.model.data.CtLocation
import com.gurpgork.countthis.core.model.data.Increment
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant
import javax.inject.Singleton

@Singleton
interface IncrementRepository {
    suspend fun addIncrement(
        counterId: Long,
        location: CtLocation?,
//        timestamp: OffsetDateTime,
        instantTime: Instant,
        isDecrement: Boolean = false,
    )

    suspend fun getIncrements(counterId: Long) : Flow<List<Increment>>

//    suspend fun save(increment: IncrementEntity) : Int
//    suspend fun save(increments: List<IncrementEntity>) = incrementDao.insertOrUpdate(increments)
}