package com.gurpgork.countthis.core.domain.interactors

import com.gurpgork.countthis.core.data.repository.CounterRepository
import com.gurpgork.countthis.core.network.AppCoroutineDispatchers
import com.gurpgork.countthis.core.domain.Interactor
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ResetCounter @Inject constructor(
    private val counterRepository: CounterRepository,
    private val dispatchers: AppCoroutineDispatchers,
) : Interactor<ResetCounter.Params>() {
    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            counterRepository.resetCounter(params.counterId)
        }
    }

    data class Params(val counterId: Long)
}