package com.gurpgork.countthis.core.domain.interactors

import com.gurpgork.countthis.core.data.repository.CounterRepository
import com.gurpgork.countthis.core.domain.Interactor
import com.gurpgork.countthis.core.model.data.Counter
import com.gurpgork.countthis.core.network.AppCoroutineDispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpsertCounter @Inject constructor(
    private val counterRepository: CounterRepository,
    private val dispatchers: AppCoroutineDispatchers,
) : Interactor<UpsertCounter.Params>(){
    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            counterRepository.upsertCounter(params.counter)
        }
    }
    data class Params(val counter: Counter)
}