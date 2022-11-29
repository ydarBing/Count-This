package com.gurpgork.countthis.ui_create

sealed class CounterFormEvent {
    data class NameChanged(val name: String) : CounterFormEvent()
    data class CountChanged(val count: Int) : CounterFormEvent()
    data class GoalChanged(val goal: Int) : CounterFormEvent()
    data class IncrementChanged(val increment: Int) : CounterFormEvent()
    data class TrackLocationChanged(val isTracking: Boolean) : CounterFormEvent()

    object Submit : CounterFormEvent()
}