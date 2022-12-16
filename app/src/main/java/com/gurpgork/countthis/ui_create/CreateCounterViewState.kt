package com.gurpgork.countthis.ui_create

import com.gurpgork.countthis.compose.UiMessage

//@Immutable
data class CreateCounterViewState (
    var form: CounterFormViewState,
    val message: UiMessage? = null,
)
{
    companion object {
        val Empty = CreateCounterViewState(CounterFormViewState.Empty)
    }
}
