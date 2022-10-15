package com.gurpgork.countthis.domain.interactors

import com.gurpgork.countthis.data.repositories.IncrementRepository
import com.gurpgork.countthis.domain.Interactor
import com.gurpgork.countthis.location.Location
import com.gurpgork.countthis.util.AppCoroutineDispatchers
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.OffsetDateTime
import javax.inject.Inject

class DecrementCounter @Inject constructor(
    private val incrementRepository: IncrementRepository,
    private val dispatchers: AppCoroutineDispatchers
) : Interactor<DecrementCounter.Params>(){

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io){
            //counterRepository.(params.counterEntity)
            incrementRepository.addIncrement(
                params.counterId,
                params.location,
                params.timestamp,
                params.instantTime,
                true)
        }
    }

    data class Params(val counterId: Long, val location: Location?, val timestamp: OffsetDateTime, val instantTime: Instant)
}