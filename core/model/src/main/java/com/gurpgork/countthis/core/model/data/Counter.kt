package com.gurpgork.countthis.core.model.data

import kotlinx.datetime.Instant
/**
 * External data layer representation of a Ct Counter
 */
data class Counter (
    val id: Long,
    val name: String,
    val increment: Int,
    val count: Int,
    val goal: Int,
    val creationDate: Instant,
    val listIndex: Int,
    val trackLocation: Boolean,
)
const val INVALID_LIST_INDEX = -1
const val INVALID_COUNTER_ID = -1L
const val CREATE_COUNTER_ID = 0L