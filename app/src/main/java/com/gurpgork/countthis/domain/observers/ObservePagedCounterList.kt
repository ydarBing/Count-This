package com.gurpgork.countthis.domain.observers

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.gurpgork.countthis.data.daos.CounterDao
import com.gurpgork.countthis.data.resultentities.CounterWithIncrementInfo
import com.gurpgork.countthis.domain.PagingInteractor
import com.gurpgork.countthis.util.SortOption
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObservePagedCounterList @Inject constructor(
    private val counterDao: CounterDao
) :PagingInteractor<ObservePagedCounterList.Params, CounterWithIncrementInfo>(){

    override fun createObservable(
        params: Params
    ): Flow<PagingData<CounterWithIncrementInfo>> = Pager(config = params.pagingConfig){
        counterDao.observePagedList(params.sort)
    }.flow

    data class Params(
        val sort: SortOption,
        override val pagingConfig: PagingConfig,
    ):Parameters<CounterWithIncrementInfo>
}