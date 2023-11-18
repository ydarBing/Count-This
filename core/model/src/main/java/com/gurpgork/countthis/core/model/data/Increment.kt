package com.gurpgork.countthis.core.model.data

import kotlinx.datetime.Instant
import java.time.ZoneId

data class Increment (
    val id: Long = 0,
    val counterId: Long,
    // TODO if millisecond precision is needed, take out default value and add Instant.now() to params
    // CURRENT_TIMESTAMP is the current date and time in UTC.
    // to get the default value, need to use @query with insert statement
    // default value is converted from seconds to milliseconds
    val incrementDate: Instant,
    val zoneId: ZoneId? = null,
    val increment: Int? = null,
    val longitude: Double? = null,
    val latitude: Double? = null,
    val altitude: Double? = null,
    val accuracy: Float? = null,
)