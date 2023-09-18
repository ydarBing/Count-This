package com.gurpgork.countthis.feature.counterdetails

import com.gurpgork.countthis.core.data.model.CounterWithIncrementsAndHistory
import com.gurpgork.countthis.core.designsystem.component.UiMessage

internal data class CounterDetailsViewState (
    //TODO change to valid counter with lists of increments and histories?
    val counterInfo: CounterWithIncrementsAndHistory? = null,
    val message: UiMessage? = null,
) {
    companion object {
        val Empty = CounterDetailsViewState()
    }
}