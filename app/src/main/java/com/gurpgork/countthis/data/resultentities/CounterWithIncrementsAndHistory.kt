package com.gurpgork.countthis.data.resultentities

import androidx.room.Embedded
import androidx.room.Ignore
import androidx.room.Relation
import com.gurpgork.countthis.counter.HistoryEntity
import com.gurpgork.countthis.counter.IncrementEntity
import com.gurpgork.countthis.data.entities.CounterEntity
import java.util.*

/// One-to-many relationship between counter and increments
/// One-to-many relationship between counter and history
/*
    Add the @Relation annotation to the instance of the child entity,
    - parentColumn set to the name of the primary key column of the parent entity
    - entityColumn set to the name of the column of the child entity that references
       the parent entity's primary key.
 */
class CounterWithIncrementsAndHistory {
    @Embedded
    lateinit var counter: CounterEntity

    @Relation(parentColumn = "id", entityColumn = "counter_id")
    var increments: List<IncrementEntity> = emptyList()
    @Relation( parentColumn = "id", entityColumn = "counter_id")
    var history: List<HistoryEntity> = emptyList()

    @delegate:Ignore
    val hasIncrements by lazy { increments.isNotEmpty() }

    @delegate:Ignore
    val hasLocations by lazy {
        increments.any { it.longitude != null }
    }

    @delegate:Ignore
    val hasHistory by lazy { history.isNotEmpty() }



    override fun equals(other: Any?): Boolean = when {
        other === this -> true
        other is CounterWithIncrementsAndHistory ->
            counter == other.counter && increments == other.increments && history == other.history
        else -> false
    }
    override fun hashCode(): Int = Objects.hash(counter, increments, history)
}

