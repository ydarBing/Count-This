package com.gurpgork.countthis.ui_create

import com.gurpgork.countthis.data.entities.CounterEntity

//@Immutable
data class CreateCounterViewState (
    //var counterId : Int = 0,
    var name: String = "",
    var trackLocation: Boolean = false,
    var startCount: Int = 0,
    var goal: Int = 0,
    var incrementBy: Int = 1,
    //var counterEntity: CounterEntity = CounterEntity.EMPTY_COUNTER
    val hasNameError: Boolean = false,
    val hasTrackLocationError: Boolean = false,
    val hasStartCountError: Boolean = false,
    val hasGoalError: Boolean = false,
    val hasIncrementError: Boolean = false,
)
{
    companion object {
        val Empty = CreateCounterViewState()
    }
}

fun CreateCounterViewState.toCounterEntity() =
    CounterEntity(
        name = name,
        increment = incrementBy,
        count = startCount,
        goal = goal,
        list_index = 0,
        track_location = trackLocation,
    )