package com.gurpgork.countthis.core.model.data

import kotlinx.datetime.Instant

data class History (
    val id: Long = 0,
    val counterId: Long,
    val count: Int? = null,
    val startDate: Instant,
    val endDate: Instant,
//    val startDate: OffsetDateTime,
//    val endDate: OffsetDateTime,
)