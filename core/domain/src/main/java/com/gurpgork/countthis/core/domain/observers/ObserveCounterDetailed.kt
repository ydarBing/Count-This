package com.gurpgork.countthis.core.domain.observers

import com.gurpgork.countthis.core.data.model.CounterWithIncrementsAndHistory
import com.gurpgork.countthis.core.data.repository.CounterRepository
import com.gurpgork.countthis.core.domain.SubjectInteractor
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/*
    Observe all information collected on a single counter
*/
class ObserveCounterDetailed @Inject constructor(
    private val counterRepository: CounterRepository,
) : SubjectInteractor<ObserveCounterDetailed.Params, CounterWithIncrementsAndHistory?>(){

    override fun createObservable(
        params: Params
    ): Flow<CounterWithIncrementsAndHistory?>{
        return counterRepository.observeCounterWithIncrementsAndHistory(params.id)
    }
    data class Params(val id: Long)
}