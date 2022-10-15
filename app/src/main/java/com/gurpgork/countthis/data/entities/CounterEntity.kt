package com.gurpgork.countthis.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.gurpgork.countthis.data.CountThisEntity
import java.time.OffsetDateTime

@Entity(tableName = "counters")
data class CounterEntity(

    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") override val id: Long = 0,
    @ColumnInfo(name = "name") val name: String = "",
    @ColumnInfo(name = "increment") val increment: Int = 1,
    @ColumnInfo(name = "count") val count: Int = 0,
    @ColumnInfo(name = "goal") val goal: Int = 0,
    @ColumnInfo(name = "created") val creation_date_time: OffsetDateTime? = null,
    @ColumnInfo(name = "list_index") val list_index: Int? = null,
    @ColumnInfo(name = "track_location") val track_location: Boolean? = null
): CountThisEntity {
    @Ignore
    constructor() : this(0)

    companion object {
        val EMPTY_COUNTER = CounterEntity()
    }
}