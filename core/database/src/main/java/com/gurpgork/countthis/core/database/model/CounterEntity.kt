package com.gurpgork.countthis.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gurpgork.countthis.core.model.data.Counter
import kotlinx.datetime.Instant
import java.time.OffsetDateTime

@Entity(tableName = "counters")
data class CounterEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long = 0,
    @ColumnInfo(name = "name") val name: String = "",
    @ColumnInfo(name = "increment") val increment: Int = 1,
    @ColumnInfo(name = "count") val count: Int = 0,
    @ColumnInfo(name = "goal") val goal: Int = 0,
    @ColumnInfo(name = "created") val creationDateTime: OffsetDateTime,
    @ColumnInfo(name = "created_instant") val creationInstant: Instant,
    @ColumnInfo(name = "list_index") val listIndex: Int? = null,
    @ColumnInfo(name = "track_location") val trackLocation: Boolean? = null
){
    constructor(counter: Counter) : this(
        id = counter.id,
        name = counter.name,
        increment = counter.increment,
        creationDateTime = counter.creationDateTime,
        creationInstant = counter.creationInstant,
        goal = counter.goal,
        count = counter.count,
        listIndex = counter.listIndex,
        trackLocation = counter.trackLocation
    )
}

fun CounterEntity.asExternalModel() = Counter(
    id = id,
    name = name,
    increment = increment,
    count = count,
    goal = goal,
    creationDateTime = creationDateTime,
    creationInstant = creationInstant,
    listIndex = listIndex ?: -1,
    trackLocation = trackLocation ?: false,
)