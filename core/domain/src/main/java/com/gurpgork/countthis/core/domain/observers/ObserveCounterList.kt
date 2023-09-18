package com.gurpgork.countthis.core.domain.observers

import com.gurpgork.countthis.core.data.model.CounterWithIncrementInfo
import com.gurpgork.countthis.core.data.repository.CounterRepository
import com.gurpgork.countthis.core.domain.SubjectInteractor
import com.gurpgork.countthis.core.model.data.SortOption
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveCounterList @Inject constructor(
    private val counterRepository: CounterRepository,
) : SubjectInteractor<ObserveCounterList.Params, List<CounterWithIncrementInfo>>() {

    override fun createObservable(
        params: Params
    ): Flow<List<CounterWithIncrementInfo>> =
        counterRepository.observeCountersWithInfo(params.sort)


    data class Params(
        val sort: SortOption,
    )
}
