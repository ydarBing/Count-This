package com.gurpgork.countthis.ui_edit

import com.gurpgork.countthis.compose.UiMessage
import com.gurpgork.countthis.ui_create.CreateCounterViewState

data class EditCounterViewState(
    var counterInfo: CreateCounterViewState,//? = null,
    val message: UiMessage? = null,
) {
    companion object {
        val Empty = EditCounterViewState(CreateCounterViewState.Empty)
    }
}

fun EditCounterViewState.toCreateCounterViewState() =
    CreateCounterViewState(
        name = counterInfo.name,
        trackLocation = counterInfo.trackLocation,
        startCount = counterInfo.startCount,
        goal = counterInfo.goal,
        incrementBy = counterInfo.incrementBy,
    )

