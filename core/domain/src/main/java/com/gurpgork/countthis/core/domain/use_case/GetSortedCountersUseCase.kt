package com.gurpgork.countthis.core.domain.use_case

import com.gurpgork.countthis.core.data.model.CounterWithIncrementInfo
import com.gurpgork.countthis.core.data.repository.CounterRepository
import com.gurpgork.countthis.core.domain.util.CounterOrder
import com.gurpgork.countthis.core.domain.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetSortedCountersUseCase @Inject constructor(
    private val counterRepository: CounterRepository,
) {
    operator fun invoke(
        counterOrder: CounterOrder = CounterOrder.Created(OrderType.Descending)
    ): Flow<List<CounterWithIncrementInfo>> {
        return counterRepository.observeCountersWithInfo().map { counters ->
            when (counterOrder.orderType) {
                is OrderType.Ascending -> {
                    when (counterOrder) {
                        is CounterOrder.Created -> counters.sortedBy { it.counter.creationDate }
                        is CounterOrder.Incremented -> counters.sortedBy { it.mostRecentIncrementDate }
                        is CounterOrder.Name -> counters.sortedBy { it.counter.name.lowercase() }
                        is CounterOrder.Custom -> counters.sortedBy { it.counter.listIndex }
                    }
                }

                is OrderType.Descending -> {
                    when (counterOrder) {
                        is CounterOrder.Created -> counters.sortedByDescending { it.counter.creationDate }
                        is CounterOrder.Incremented -> counters.sortedByDescending { it.mostRecentIncrementDate }
                        is CounterOrder.Name -> counters.sortedByDescending { it.counter.name.lowercase() }
                        is CounterOrder.Custom -> counters.sortedByDescending { it.counter.listIndex }
                    }
                }
            }
        }
    }
}