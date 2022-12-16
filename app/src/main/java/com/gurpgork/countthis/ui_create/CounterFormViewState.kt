package com.gurpgork.countthis.ui_create

data class CounterFormViewState (
    var name: String = "",
    var trackLocation: Boolean = false,
    var startCount: Int = 0,
    var goal: Int = 0,
    var incrementBy: Int = 1,
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