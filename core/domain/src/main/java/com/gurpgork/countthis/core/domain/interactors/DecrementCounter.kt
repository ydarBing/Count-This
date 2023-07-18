package com.gurpgork.countthis.core.domain.interactors

import com.gurpgork.countthis.core.data.repository.IncrementRepository
import com.gurpgork.countthis.core.network.AppCoroutineDispatchers
import com.gurpgork.countthis.core.domain.Interactor
import com.gurpgork.countthis.core.model.data.CtLocation
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
                params.locationEntity,
                params.timestamp,
                params.instantTime,
                true)
        }
    }

    data class Params(
        val counterId: Long,
        val locationEntity: CtLocation?,
        val timestamp: OffsetDateTime,
        val instantTime: Instant
    )
}