package com.gurpgork.countthis.core.domain.use_case

import com.gurpgork.countthis.core.domain.GetCounter
import com.gurpgork.countthis.core.domain.interactors.AddCounter
import com.gurpgork.countthis.core.domain.interactors.DecrementCounter
import com.gurpgork.countthis.core.domain.interactors.DeleteCounter
import com.gurpgork.countthis.core.domain.interactors.IncrementCounter
import com.gurpgork.countthis.core.domain.interactors.ResetCounter
import com.gurpgork.countthis.core.domain.interactors.UpdateCounter
import com.gurpgork.countthis.core.domain.observers.ObserveCounter
import com.gurpgork.countthis.core.domain.observers.ObservePagedCounterList

// TODO this needs to be injected in a module...
data class CounterUseCases (
    val getCounter: GetCounter,
    val observeCounter: ObserveCounter,
    val getSortedCounters: GetSortedCountersUseCase,
    val observePagedCounterList: ObservePagedCounterList,

    val addCounter: AddCounter,
    val deleteCounter: DeleteCounter,
    val incrementCounter: IncrementCounter,
    val decrementCounter: DecrementCounter,
    val resetCounter: ResetCounter,
    val updateCounter: UpdateCounter,
)