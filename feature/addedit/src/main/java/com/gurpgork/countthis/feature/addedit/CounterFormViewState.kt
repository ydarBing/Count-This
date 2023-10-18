package com.gurpgork.countthis.feature.addedit

import com.gurpgork.countthis.core.model.data.CREATE_COUNTER_ID
import com.gurpgork.countthis.core.model.data.Counter
import kotlinx.datetime.Instant

data class CounterFormViewState(
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

fun CounterFormViewState.asExternalModel(
    listIndex: Int,
    creationDate: Instant,
    id:Long = CREATE_COUNTER_ID,
) = Counter(
    id = id,
    listIndex = listIndex,
    name = name.ifBlank { namePlaceholder },
    increment = incrementBy.toIntOrNull() ?: incrementByPlaceholder.toInt(),
    count = startCount.toIntOrNull() ?: startCountPlaceholder.toInt(),
    goal = goal.toIntOrNull() ?: goalPlaceholder.toInt(),
    trackLocation = trackLocation,
    creationDate = creationDate,
)