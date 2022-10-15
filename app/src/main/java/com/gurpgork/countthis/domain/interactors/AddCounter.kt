package com.gurpgork.countthis.domain.interactors

import com.gurpgork.countthis.data.entities.CounterEntity
import com.gurpgork.countthis.counter.CounterRepository
import com.gurpgork.countthis.domain.Interactor
import com.gurpgork.countthis.util.AppCoroutineDispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddCounter @Inject constructor(
    private val counterRepository: CounterRepository,
    private val dispatchers: AppCoroutineDispatchers
) : Interactor<AddCounter.Params>(){

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io){
            counterRepository.insertCounter(params.counterEntity)
        }
    }

    data class Params(val counterEntity: CounterEntity)
}