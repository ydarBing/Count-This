package com.gurpgork.countthis.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.gurpgork.countthis.core.model.data.History
import java.time.OffsetDateTime

@Entity(
    tableName = "history",
    indices = [Index(value = ["counter_id"])],
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
data class HistoryEntity (
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long = 0,
    @ColumnInfo(name = "counter_id") val counterId: Long,
    @ColumnInfo(name = "count") val count: Int? = null,
    @ColumnInfo(name = "start_date") val startDate: OffsetDateTime,
    @ColumnInfo(name = "end_date") val endDate: OffsetDateTime,
)

fun HistoryEntity.asExternalModel() = History(
    id = id,
    counterId = counterId,
    count = count,
    startDate = startDate,
    endDate = endDate
)