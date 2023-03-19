package com.gurpgork.countthis.ui_create

import com.gurpgork.countthis.data.entities.CounterEntity

data class CounterFormViewState (
    var name: String = "",
    var trackLocation: Boolean = false,
    var startCount: String = "",//Int? = 0,
    var goal: String = "", //Int = 0,
    var incrementBy: String = "", //Int = 1,

    val namePlaceholder: String = CounterEntity.EMPTY_COUNTER.name,
    val startCountPlaceholder: String = CounterEntity.EMPTY_COUNTER.count.toString(),
    val goalPlaceholder: String = CounterEntity.EMPTY_COUNTER.goal.toString(),
    val incrementByPlaceholder: String = CounterEntity.EMPTY_COUNTER.increment.toString(),

    val hasNameError: Boolean = false,
    val hasTrackLocationError: Boolean = false,
    val hasStartCountError: Boolean = false,
    val hasGoalError: Boolean = false,
    val hasIncrementError: Boolean = false,
)
{
    companion object {
        val Empty = CounterFormViewState()
    }
}