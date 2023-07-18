package com.gurpgork.countthis.core.model.data

import kotlinx.datetime.Instant
import java.time.OffsetDateTime
/**
 * External data layer representation of a Ct Counter
 */
data class Counter (
    val id: Long,
    val name: String,
    val increment: Int,
    val count: Int,
    val goal: Int,
    val creationDateTime: OffsetDateTime,
    val creationInstant: Instant,
    val listIndex: Int,
    val trackLocation: Boolean,
)