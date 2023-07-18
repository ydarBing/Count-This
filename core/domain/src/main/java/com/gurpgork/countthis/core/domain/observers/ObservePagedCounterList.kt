package com.gurpgork.countthis.core.domain.observers

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.gurpgork.countthis.core.data.model.CounterWithIncrementInfo
import com.gurpgork.countthis.core.data.repository.CounterRepository
import com.gurpgork.countthis.core.domain.PagingInteractor
import com.gurpgork.countthis.core.model.data.SortOption
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObservePagedCounterList @Inject constructor(
    private val counterRepository: CounterRepository,
) : PagingInteractor<ObservePagedCounterList.Params, CounterWithIncrementInfo>() {

    override fun createObservable(
        params: Params
    ): Flow<PagingData<CounterWithIncrementInfo>> =
        counterRepository.observeForPaging(params.pagingConfig, params.sort)


    data class Params(
        val sort: SortOption,
        override val pagingConfig: PagingConfig,
    ) : Parameters<CounterWithIncrementInfo>
}