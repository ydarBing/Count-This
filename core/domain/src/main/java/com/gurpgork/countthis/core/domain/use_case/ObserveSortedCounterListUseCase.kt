package com.gurpgork.countthis.core.domain.use_case

import com.gurpgork.countthis.core.data.model.CounterWithIncrementInfo
import com.gurpgork.countthis.core.data.repository.CounterRepository
import com.gurpgork.countthis.core.data.repository.UserDataRepository
import com.gurpgork.countthis.core.model.data.SortOption
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class ObserveSortedCounterListUseCase @Inject constructor(
    private val counterRepository: CounterRepository,
    private val userDataRepository: UserDataRepository,
) {
    operator fun invoke(): Flow<List<CounterWithIncrementInfo>> {
        return combine(
            userDataRepository.userData,
            counterRepository.observeCountersWithInfo(),
        ){ userData, counters ->
            counters.sortedWith(
                compareBy<CounterWithIncrementInfo> {
                    when(userData.currentSort){
                        SortOption.USER_SORTED -> it.counter.listIndex
                        SortOption.LAST_UPDATED -> it.mostRecentIncrementDate
                        SortOption.ALPHABETICAL -> it.counter.name.lowercase()
                        SortOption.DATE_ADDED -> it.counter.creationDate
                    }
                }.thenBy {
                    if (userData.currentSort == SortOption.ALPHABETICAL)
                        it.mostRecentIncrementDate
                    else
                        it.counter.name.lowercase()
                }
            )
        }
    }
}