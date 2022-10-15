package com.gurpgork.countthis.domain.interactors

import com.gurpgork.countthis.counter.CounterRepository
import com.gurpgork.countthis.data.repositories.IncrementRepository
import com.gurpgork.countthis.domain.Interactor
import com.gurpgork.countthis.location.Location
import com.gurpgork.countthis.util.AppCoroutineDispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.OffsetDateTime
import javax.inject.Inject

class IncrementCounter @Inject constructor(
    private val counterRepository: CounterRepository,
    private val incrementRepository: IncrementRepository,
    private val dispatchers: AppCoroutineDispatchers
) : Interactor<IncrementCounter.Params>(){

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io){
            incrementRepository.addIncrement(
                params.counterId,
                params.location,
                params.timestamp,
                params.instantTime)
            ensureActive()
        }
    }

    data class Params(val counterId: Long, val location: Location?, val timestamp: OffsetDateTime, val instantTime: Instant)
}