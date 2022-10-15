package com.gurpgork.countthis.domain.interactors

//class UpdateCounter @Inject constructor(
//    private val counterDao: CounterDao,
//    private val dispatchers: AppCoroutineDispatchers,
//) : Interactor<UpdateCounter.Params>(){
//    override suspend fun doWork(params: Params) {
//        withContext(dispatchers.io) {
//            counterDao.update(params.counterId)
//        }
//    }
//    data class Params(val counterId: Long)
//}