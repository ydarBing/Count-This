package com.gurpgork.countthis.core.domain.interactors

import com.gurpgork.countthis.core.data.repository.CounterRepository
import com.gurpgork.countthis.core.domain.Interactor
import com.gurpgork.countthis.core.network.AppCoroutineDispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteCounter @Inject constructor(
    private val counterRepository: CounterRepository,
    private val dispatchers: AppCoroutineDispatchers,
) : Interactor<DeleteCounter.Params>(){
    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            counterRepository.deleteAndUpdateListIndices(params.counterId, params.listIndex)
        }
    }
    data class Params(val counterId: Long, val listIndex: Int)
}