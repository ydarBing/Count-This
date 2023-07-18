package com.gurpgork.countthis.core.model.data

import java.time.OffsetDateTime

data class History (
    val id: Long = 0,
    val counterId: Long,
    val count: Int? = null,
    val startDate: OffsetDateTime,
    val endDate: OffsetDateTime,
)