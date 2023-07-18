package com.gurpgork.countthis.core.domain.interactors

import com.gurpgork.countthis.core.data.repository.CounterRepository
import com.gurpgork.countthis.core.network.AppCoroutineDispatchers
import com.gurpgork.countthis.core.domain.Interactor
import com.gurpgork.countthis.core.model.data.Counter
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateCounter @Inject constructor(
    private val counterRepository: CounterRepository,
    private val dispatchers: AppCoroutineDispatchers,
) : Interactor<UpdateCounter.Params>(){
    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            counterRepository.updateCounter(params.counter)
        }
    }
    data class Params(val counter: Counter)
}