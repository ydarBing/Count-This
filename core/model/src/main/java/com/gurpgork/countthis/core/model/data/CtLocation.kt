package com.gurpgork.countthis.core.model.data

/**
 * External data layer representation of a Ct Location
 */
data class CtLocation (
    val id: Int,
    val time: Long,
    val latitude: Double,
    val longitude: Double,
    val altitude: Double,
    val accuracy: Float,
)