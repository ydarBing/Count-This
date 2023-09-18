package com.gurpgork.countthis.feature.addedit

import com.gurpgork.countthis.core.designsystem.component.UiMessage

data class AddEditCounterViewState(
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
//        val Empty = AddEditCounterViewState(CounterFormViewState.Empty)
//    }
//}

//fun AddEditCounterViewState.toCounter(id: Long) =
//    Counter(
//        id = id,
//        name = form.name,
//        increment = form.incrementBy,
//        count = form.startCount,
//        goal = form.goal,
//        list_index = 0,
//        track_location = form.trackLocation,
//    )
