package com.gurpgork.countthis.feature.editcreate.create

import com.gurpgork.countthis.core.designsystem.component.UiMessage
import com.gurpgork.countthis.feature.editcreate.CounterFormViewState

//@Immutable
data class CreateCounterViewState (
    var form: CounterFormViewState,
    val message: UiMessage? = null,
)
//{
//    companion object {
//        val Empty = CreateCounterViewState(CounterFormViewState.Empty)
//    }
//}
