package com.gurpgork.countthis.domain.observers

import com.gurpgork.countthis.data.daos.CounterDao
import com.gurpgork.countthis.data.resultentities.CounterWithIncrementsAndHistory
import com.gurpgork.countthis.domain.SubjectInteractor
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/*
    Observe all information collected on a single counter
*/
class ObserveCounterDetailed @Inject constructor(
    private val dao: CounterDao
) :SubjectInteractor<ObserveCounterDetailed.Params, CounterWithIncrementsAndHistory?>(){

    override fun createObservable(
        params: Params
    ): Flow<CounterWithIncrementsAndHistory?>{
        return dao.observeCounterWithIncrementsAndHistory(params.id)
    }
    data class Params(val id: Long)
}