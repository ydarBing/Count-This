package com.gurpgork.countthis.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.gurpgork.countthis.core.model.data.Increment
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset

@Entity(
    tableName = "increments",
    indices = [Index(value = ["counter_id", "date_epoch"])],
    foreignKeys = [
        ForeignKey(
            entity = com.gurpgork.countthis.core.database.model.CounterEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("counter_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class IncrementEntity(

    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long = 0,
    @ColumnInfo(name = "counter_id") val counterId: Long,
    @ColumnInfo(name = "date") val date: OffsetDateTime,//? = null,
    // TODO if millisecond precision is needed, take out default value and add Instant.now() to params
    // CURRENT_TIMESTAMP is the current date and time in UTC.
    // to get the default value, need to use @query with insert statement
    // default value is converted from seconds to milliseconds
    @ColumnInfo(name = "date_epoch", defaultValue = "(1000 * strftime('%s','now'))")
    val instantUTC: Long,
    @ColumnInfo(name = "time_zone_id")
    val zoneId: ZoneId? = null,
    @ColumnInfo(name = "time_zone_offset")
    val zoneOffset: ZoneOffset? = null,
//    @ColumnInfo(name = "fixed_offset_time_zone")
//    val fixedOffsetTimeZone: FixedOffsetTimeZone? = null,
    @ColumnInfo(name = "increment") val increment: Int? = null,
    @ColumnInfo(name = "longitude") val longitude: Double? = null,
    @ColumnInfo(name = "latitude") val latitude: Double? = null,
    @ColumnInfo(name = "altitude") val altitude: Double? = null,
    @ColumnInfo(name = "accuracy") val accuracy: Float? = null,
) : Comparable<IncrementEntity> {

    override fun compareTo(other: IncrementEntity): Int {
        return date.compareTo(other.date)
    }
}

fun IncrementEntity.asExternalModel() = Increment(
    id = id,
    counterId = counterId,
    date = date,
    instantUTC = instantUTC,
    zoneId = zoneId,
//    zoneOffset = zoneOffset,
    increment = increment,
    longitude = longitude,
    latitude = latitude,
    altitude = altitude,
    accuracy = accuracy,
)