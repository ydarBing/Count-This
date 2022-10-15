package com.gurpgork.countthis.location

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location_table")
data class Location(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val time: Long,
    val latitude: Double,
    val longitude: Double,
    val altitude: Double,
    val accuracy: Float,
)