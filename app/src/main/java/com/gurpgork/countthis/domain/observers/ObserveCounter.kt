package com.gurpgork.countthis.domain.observers

import com.gurpgork.countthis.data.daos.CounterDao
import com.gurpgork.countthis.data.entities.CounterEntity
import com.gurpgork.countthis.domain.SubjectInteractor
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveCounter @Inject constructor(
    private val dao: CounterDao
) :SubjectInteractor<ObserveCounter.Params, CounterEntity>(){

    override fun createObservable(
        params: Params
    ): Flow<CounterEntity>{
        return dao.observeCounter(params.id)
    }
    data class Params(val id: Long)
}