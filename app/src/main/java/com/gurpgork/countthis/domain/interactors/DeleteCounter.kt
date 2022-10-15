package com.gurpgork.countthis.domain.interactors

import com.gurpgork.countthis.counter.CounterRepository
import com.gurpgork.countthis.domain.Interactor
import com.gurpgork.countthis.util.AppCoroutineDispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteCounter @Inject constructor(
    private val counterRepository: CounterRepository,
    private val dispatchers: AppCoroutineDispatchers,
) : Interactor<DeleteCounter.Params>(){
    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            counterRepository.deleteWithId(params.counterId)
        }
    }
    data class Params(val counterId: Long)
}