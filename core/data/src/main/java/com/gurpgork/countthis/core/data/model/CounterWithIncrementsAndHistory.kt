package com.gurpgork.countthis.core.data.model

import com.gurpgork.countthis.core.database.model.HistoryEntity
import com.gurpgork.countthis.core.database.model.IncrementEntity
import com.gurpgork.countthis.core.database.model.asExternalModel
import com.gurpgork.countthis.core.database.resultentities.CounterWithIncrementsAndHistoryEntity
import com.gurpgork.countthis.core.model.data.Counter
import com.gurpgork.countthis.core.model.data.History
import com.gurpgork.countthis.core.model.data.Increment

data class CounterWithIncrementsAndHistory(
    val counter: Counter,
    val increments: List<Increment>,
    val history: List<History>,
    val hasIncrements: Boolean,
    val hasLocations: Boolean,
    val hasHistory: Boolean,
)

fun CounterWithIncrementsAndHistoryEntity.asExternalModel() = CounterWithIncrementsAndHistory(
    counter = counter.asExternalModel(),
    increments = increments.map(IncrementEntity::asExternalModel),
    history = history.map(HistoryEntity::asExternalModel),
    hasIncrements = hasIncrements,
    hasLocations = hasLocations,
    hasHistory = hasHistory
)

