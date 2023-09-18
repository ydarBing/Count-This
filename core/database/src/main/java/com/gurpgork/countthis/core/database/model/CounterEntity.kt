package com.gurpgork.countthis.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gurpgork.countthis.core.model.data.Counter
import kotlinx.datetime.Instant

@Entity(tableName = "counters")
data class CounterEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long = 0,
    @ColumnInfo(name = "name") val name: String = "",
    @ColumnInfo(name = "increment") val increment: Int = 1,
    @ColumnInfo(name = "count") val count: Int = 0,
    @ColumnInfo(name = "goal") val goal: Int = 0,
    @ColumnInfo(name = "creation_date") val creationDate: Instant,
    @ColumnInfo(name = "list_index") val listIndex: Int? = null,
    @ColumnInfo(name = "track_location") val trackLocation: Boolean? = null
){
    constructor(counter: Counter) : this(
        id = counter.id,
        name = counter.name,
        increment = counter.increment,
        creationDate = counter.creationDate,  //Clock.System.now()
        goal = counter.goal,
        count = counter.count,
        listIndex = counter.listIndex,
        trackLocation = counter.trackLocation,
    )
}

fun CounterEntity.asExternalModel() = Counter(
    id = id,
    name = name,
    increment = increment,
    count = count,
    goal = goal,
    creationDate = creationDate,
    listIndex = listIndex ?: -1,
    trackLocation = trackLocation ?: false,
)