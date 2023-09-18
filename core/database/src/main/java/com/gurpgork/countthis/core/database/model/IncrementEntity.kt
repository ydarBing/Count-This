package com.gurpgork.countthis.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.gurpgork.countthis.core.model.data.Increment
import kotlinx.datetime.Instant
import java.time.ZoneId

@Entity(
    tableName = "increments",
    indices = [Index(value = ["counter_id", "increment_date"])],
    foreignKeys = [
        ForeignKey(
            entity = CounterEntity::class,
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
//    @ColumnInfo(name = "date") val date: OffsetDateTime,//? = null,
    // TODO if millisecond precision is needed, take out default value and add Instant.now() to params
    // CURRENT_TIMESTAMP is the current date and time in UTC.
    // to get the default value, need to use @query with insert statement
    // default value is converted from seconds to milliseconds
    @ColumnInfo(name = "increment_date")//, defaultValue = "(1000 * strftime('%s','now'))")
    val incrementDate: Instant,
    @ColumnInfo(name = "time_zone_id")
    val zoneId: ZoneId? = null,
    @ColumnInfo(name = "increment") val increment: Int? = null,
    @ColumnInfo(name = "longitude") val longitude: Double? = null,
    @ColumnInfo(name = "latitude") val latitude: Double? = null,
    @ColumnInfo(name = "altitude") val altitude: Double? = null,
    @ColumnInfo(name = "accuracy") val accuracy: Float? = null,
) : Comparable<IncrementEntity> {

    override fun compareTo(other: IncrementEntity): Int {
        return incrementDate.compareTo(other.incrementDate)
//        return date.compareTo(other.date)
    }
}

fun IncrementEntity.asExternalModel() = Increment(
    id = id,
    counterId = counterId,
//    date = date,
    incrementDate = incrementDate,
    zoneId = zoneId,
//    zoneOffset = zoneOffset,
    increment = increment,
    longitude = longitude,
    latitude = latitude,
    altitude = altitude,
    accuracy = accuracy,
)