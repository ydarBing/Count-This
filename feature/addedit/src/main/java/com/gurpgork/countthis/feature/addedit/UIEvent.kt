package com.gurpgork.countthis.feature.addedit

sealed class UIEvent{
    data class NameChanged(val name: String): UIEvent()
    data class CountChanged(val count: Int): UIEvent()
    data class IncrementChanged(val increment: Int): UIEvent()
    data class GoalChanged(val goal: Int): UIEvent()
    data class TrackLocationChanged(val trackLocation: Boolean): UIEvent()

    object Submit: UIEvent()
}