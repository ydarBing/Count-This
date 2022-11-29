package com.gurpgork.countthis.domain.interactors

import com.gurpgork.countthis.data.daos.CounterDao
import com.gurpgork.countthis.data.entities.CounterEntity
import com.gurpgork.countthis.domain.Interactor
import com.gurpgork.countthis.util.AppCoroutineDispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateCounter @Inject constructor(
    private val counterDao: CounterDao,
    private val dispatchers: AppCoroutineDispatchers,
) : Interactor<UpdateCounter.Params>(){
    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            counterDao.update(params.counter)
        }
    }
    data class Params(val counter: CounterEntity)
}