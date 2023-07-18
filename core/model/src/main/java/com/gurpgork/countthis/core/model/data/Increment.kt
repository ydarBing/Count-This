package com.gurpgork.countthis.core.model.data

import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset

data class Increment (
    val id: Long = 0,
    val counterId: Long,
    val date: OffsetDateTime,//? = null,
    // TODO if millisecond precision is needed, take out default value and add Instant.now() to params
    // CURRENT_TIMESTAMP is the current date and time in UTC.
    // to get the default value, need to use @query with insert statement
    // default value is converted from seconds to milliseconds
    val instantUTC: Long,
    val zoneId: ZoneId? = null,
    val zoneOffset: ZoneOffset? = null,
    val increment: Int? = null,
    val longitude: Double? = null,
    val latitude: Double? = null,
    val altitude: Double? = null,
    val accuracy: Float? = null,
)