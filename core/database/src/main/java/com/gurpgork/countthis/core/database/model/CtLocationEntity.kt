package com.gurpgork.countthis.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gurpgork.countthis.core.model.data.CtLocation

@Entity(tableName = "location_table")
data class CtLocationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val time: Long,
    val latitude: Double,
    val longitude: Double,
    val altitude: Double,
    val accuracy: Float,
)

fun CtLocationEntity.asExternalModel() = CtLocation(
    id = id,
    time = time,
    latitude = latitude,
    longitude = longitude,
    altitude = altitude,
    accuracy = accuracy
)