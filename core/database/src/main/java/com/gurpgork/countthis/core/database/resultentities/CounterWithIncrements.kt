package com.gurpgork.countthis.core.database.resultentities

import androidx.room.Embedded
import androidx.room.Relation
import java.util.Objects

/// One-to-many relationship between counter and increments
/*
    Add the @Relation annotation to the instance of the child entity,
    - parentColumn set to the name of the primary key column of the parent entity
    - entityColumn set to the name of the column of the child entity that references
       the parent entity's primary key.
 */
/*abstract */ class CounterWithIncrements{
    @Embedded
    lateinit var counter: com.gurpgork.countthis.core.database.model.CounterEntity

    @Relation(
        parentColumn = "id",
        entityColumn = "counter_id"
    )
    var increments: List<com.gurpgork.countthis.core.database.model.IncrementEntity> = emptyList()
    var countOfIncrementsToday: Long = 0
    var sumOfIncrementsToday: Long = 0

    override fun equals(other: Any?): Boolean = when {
        other === this -> true
        other is CounterWithIncrements -> counter == other.counter && increments == other.increments
        else -> false
    }

    override fun hashCode(): Int = Objects.hash(counter, increments)
}

