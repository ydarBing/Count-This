package com.gurpgork.countthis.core.data.model

import com.gurpgork.countthis.core.database.model.asExternalModel
import com.gurpgork.countthis.core.database.resultentities.CounterWithIncrementInfoEntity
import com.gurpgork.countthis.core.model.data.Counter
import kotlinx.datetime.Instant

data class CounterWithIncrementInfo(
    val counter: Counter,
    val incrementsTodayCount: Long = 0,
    val incrementsTodaySum: Long = 0,
//    val mostRecentIncrement: OffsetDateTime,
    val mostRecentIncrementDate: Instant? = null,
//    val mostRecentIncrementInstantOffset: ZoneOffset? = null,
)

fun CounterWithIncrementInfoEntity.asExternalModel() = CounterWithIncrementInfo(
    counter = counter.asExternalModel(),
    incrementsTodayCount = incrementsTodayCount,
    incrementsTodaySum = incrementsTodaySum,
//    mostRecentIncrement = mostRecentIncrement,
    mostRecentIncrementDate = mostRecentIncrementDate,
//    mostRecentIncrementInstantOffset = mostRecentIncrementInstantOffset
)

