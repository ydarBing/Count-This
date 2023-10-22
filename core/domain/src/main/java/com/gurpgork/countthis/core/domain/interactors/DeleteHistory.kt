package com.gurpgork.countthis.core.domain.interactors

import com.gurpgork.countthis.core.data.repository.HistoryRepository
import com.gurpgork.countthis.core.domain.Interactor
import com.gurpgork.countthis.core.network.AppCoroutineDispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteHistory @Inject constructor(
    private val historyRepository: HistoryRepository,
    private val dispatchers: AppCoroutineDispatchers,
) : Interactor<DeleteHistory.Params>(){
    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            historyRepository.deleteHistory(params.historyIds)
        }
    }
    data class Params(val historyIds: Set<Long>)
}