package com.gurpgork.countthis.feature.editcreate.edit

import com.gurpgork.countthis.core.designsystem.component.UiMessage
import com.gurpgork.countthis.feature.editcreate.CounterFormViewState

data class EditCounterViewState(
    val form: CounterFormViewState,
//    val name: String = "",
//    val trackLocation: Boolean = false,
//    val startCount: Int = 0,
//    val goal: Int = 0,
//    val incrementBy: Int = 1,
    val message: UiMessage? = null,
)
//{
//    companion object {
//        val Empty = EditCounterViewState(CounterFormViewState.Empty)
//    }
//}

//fun EditCounterViewState.toCounter(id: Long) =
//    Counter(
//        id = id,
//        name = form.name,
//        increment = form.incrementBy,
//        count = form.startCount,
//        goal = form.goal,
//        list_index = 0,
//        track_location = form.trackLocation,
//    )
