package com.gurpgork.countthis.core.domain.observers

import com.gurpgork.countthis.core.data.repository.CounterRepository
import com.gurpgork.countthis.core.domain.SubjectInteractor
import com.gurpgork.countthis.core.model.data.Counter
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveCounter @Inject constructor(
    private val counterRepository: CounterRepository,
) : SubjectInteractor<ObserveCounter.Params, Counter>(){

    override fun createObservable(
        params: Params
    ): Flow<Counter>{
        return counterRepository.observeCounter(params.id)
    }
    data class Params(val id: Long)
}