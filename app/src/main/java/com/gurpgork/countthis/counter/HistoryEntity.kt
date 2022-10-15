package com.gurpgork.countthis.counter

import androidx.room.*
import com.gurpgork.countthis.data.CountThisEntity
import com.gurpgork.countthis.data.entities.CounterEntity
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
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") override val id: Long = 0,
    @ColumnInfo(name = "counter_id") val counterId: Long,
    @ColumnInfo(name = "count") val count: Int? = null,
    @ColumnInfo(name = "start_date") val start_date: OffsetDateTime? = null,
    @ColumnInfo(name = "end_date") val end_date: OffsetDateTime? = null,
) : CountThisEntity {
    @Ignore
    constructor() : this(0,0)

    companion object {
        val EMPTY_HISTORY = HistoryEntity()
    }
}