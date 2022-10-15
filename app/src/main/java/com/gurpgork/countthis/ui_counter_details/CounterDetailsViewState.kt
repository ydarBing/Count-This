package com.gurpgork.countthis.ui_counter_details

import com.gurpgork.countthis.compose.UiMessage
import com.gurpgork.countthis.data.resultentities.CounterWithIncrementsAndHistory

internal data class CounterDetailsViewState (
    //TODO change to valid counter with lists of increments and histories?
    val counterInfo: CounterWithIncrementsAndHistory? = null,
    val message: UiMessage? = null,
) {
    companion object {
        val Empty = CounterDetailsViewState()
    }
}