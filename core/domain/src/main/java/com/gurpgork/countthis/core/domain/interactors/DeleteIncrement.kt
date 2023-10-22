package com.gurpgork.countthis.core.domain.interactors

import com.gurpgork.countthis.core.data.repository.IncrementRepository
import com.gurpgork.countthis.core.domain.Interactor
import com.gurpgork.countthis.core.network.AppCoroutineDispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteIncrement @Inject constructor(
    private val incrementRepository: IncrementRepository,
    private val dispatchers: AppCoroutineDispatchers,
) : Interactor<DeleteIncrement.Params>(){
    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            incrementRepository.deleteIncrements(params.incrementIds)
        }
    }
    data class Params(val incrementIds: Set<Long>)
}