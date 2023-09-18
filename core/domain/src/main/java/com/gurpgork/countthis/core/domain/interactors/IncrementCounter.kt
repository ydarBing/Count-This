package com.gurpgork.countthis.core.domain.interactors

import com.gurpgork.countthis.core.data.repository.IncrementRepository
import com.gurpgork.countthis.core.domain.Interactor
import com.gurpgork.countthis.core.model.data.CtLocation
import com.gurpgork.countthis.core.network.AppCoroutineDispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import javax.inject.Inject

class IncrementCounter @Inject constructor(
    private val incrementRepository: IncrementRepository,
    private val dispatchers: AppCoroutineDispatchers
) : Interactor<IncrementCounter.Params>(){

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io){
            incrementRepository.addIncrement(
                params.counterId,
                params.location,
                params.incrementDate,
            )
            ensureActive()
        }
    }

    data class Params(
        val counterId: Long,
        val location: CtLocation?,
        val incrementDate: Instant =  Clock.System.now(),
    )
}