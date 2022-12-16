package com.gurpgork.countthis.ui_edit

import com.gurpgork.countthis.compose.UiMessage
import com.gurpgork.countthis.data.entities.CounterEntity
import com.gurpgork.countthis.ui_create.CounterFormViewState

data class EditCounterViewState(
    val form: CounterFormViewState,
//    val name: String = "",
//    val trackLocation: Boolean = false,
//    val startCount: Int = 0,
//    val goal: Int = 0,
//    val incrementBy: Int = 1,
    val message: UiMessage? = null,
) {
    companion object {
        val Empty = EditCounterViewState(CounterFormViewState.Empty)
    }
}

fun EditCounterViewState.toCounterEntity(id: Long) =
    CounterEntity(
        id = id,
        name = form.name,
        increment = form.incrementBy,
        count = form.startCount,
        goal = form.goal,
        list_index = 0,
        track_location = form.trackLocation,
    )
