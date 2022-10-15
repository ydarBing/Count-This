package com.gurpgork.countthis.counter

import androidx.room.*
import com.gurpgork.countthis.data.CountThisEntity
import com.gurpgork.countthis.data.entities.CounterEntity
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset

@Entity(
    tableName = "increments",
    indices = [Index(value = ["counter_id", "date_epoch"])],
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

    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") override val id: Long = 0,
    @ColumnInfo(name = "counter_id") val counterId: Long,
    @ColumnInfo(name = "date") val date: OffsetDateTime,//? = null,
    // TODO if millisecond precision is needed, take out default value and add Instant.now() to params
    // CURRENT_TIMESTAMP is the current date and time in UTC.
    // to get the default value, need to use @query with insert statement
    // default value is converted from seconds to milliseconds
    @ColumnInfo(name = "date_epoch", defaultValue = "(1000 * strftime('%s','now'))")
    val instantUTC: Long? = null,
    @ColumnInfo(name = "time_zone_id")
    val zoneId: ZoneId? = null,
    @ColumnInfo(name = "time_zone_offset")
    val zoneOffset: ZoneOffset? = null,
    @ColumnInfo(name = "increment") val increment: Int? = null,
    @ColumnInfo(name = "longitude") val longitude: Double? = null,
    @ColumnInfo(name = "latitude") val latitude: Double? = null,
    @ColumnInfo(name = "altitude") val altitude: Double? = null,
    @ColumnInfo(name = "accuracy") val accuracy: Float? = null,
) : CountThisEntity, Comparable<IncrementEntity> {

    override fun compareTo(other: IncrementEntity): Int {
        return date.compareTo(other.date)
    }

//    companion object {
//        val EMPTY_INCREMENT = IncrementEntity()
//    }
}

