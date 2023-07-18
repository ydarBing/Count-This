package com.gurpgork.countthis.feature.editcreate

import com.gurpgork.countthis.core.model.data.Counter
import kotlinx.datetime.Clock
import java.time.OffsetDateTime

data class CounterFormViewState (
    var name: String = "",
    var trackLocation: Boolean = false,
    var startCount: String = "",//Int? = 0,
    var goal: String = "", //Int = 0,
    var incrementBy: String = "", //Int = 1,

    val namePlaceholder: String,
    val startCountPlaceholder: String,
    val goalPlaceholder: String,
    val incrementByPlaceholder: String,

    val hasNameError: Boolean = false,
    val hasTrackLocationError: Boolean = false,
    val hasStartCountError: Boolean = false,
    val hasGoalError: Boolean = false,
    val hasIncrementError: Boolean = false,
)
//{
//    companion object {
//        val Empty = CounterFormViewState()
//    }
//}
fun CounterFormViewState.asExternalModel() = Counter(
    id = -1,
    listIndex = -1,
    name = name,
    increment = incrementBy.toInt(),
    count = startCount.toInt(),
    goal = goal.toInt(),
    trackLocation = trackLocation,
    creationInstant = Clock.System.now(),
    creationDateTime = OffsetDateTime.now(),
)