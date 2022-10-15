package com.gurpgork.countthis.data.resultentities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import com.gurpgork.countthis.data.entities.CounterEntity
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*

class CounterWithIncrementInfo{
    @Embedded
    lateinit var counter: CounterEntity
//    @Relation(
//        parentColumn = "id",
//        entityColumn = "counter_id",
//        entity = IncrementEntity::class,
//    )

    @ColumnInfo(name = "increments_today_count")
    var incrementsTodayCount: Long = 0
    @ColumnInfo(name = "increments_today_sum")
    var incrementsTodaySum: Long = 0
    // guaranteed to be either creation date or most recent increment date
    @ColumnInfo(name = "most_recent_increment")
    lateinit var mostRecentIncrement: OffsetDateTime
    @ColumnInfo(name = "most_recent_increment_instant")
    var mostRecentIncrementInstant: Long? = null
    @ColumnInfo(name = "most_recent_increment_instant_offset")
    var mostRecentIncrementInstantOffset: ZoneOffset? = null

    /**
     * Allow consumers to destructure this class
     */
    operator fun component1() = counter
    operator fun component2() = incrementsTodayCount
    operator fun component3() = incrementsTodaySum
    operator fun component4() = mostRecentIncrement
    operator fun component5() = mostRecentIncrementInstant
    operator fun component6() = mostRecentIncrementInstantOffset


    override fun equals(other: Any?): Boolean = when {
        other === this -> true
        other is CounterWithIncrementInfo -> {
            counter == other.counter &&
                    incrementsTodayCount == other.incrementsTodayCount &&
                    incrementsTodaySum == other.incrementsTodaySum &&
                    mostRecentIncrement == other.mostRecentIncrement &&
                    mostRecentIncrementInstant == other.mostRecentIncrementInstant
        }
        else -> false
    }

    override fun hashCode(): Int = Objects.hash(counter,
        incrementsTodayCount,
        incrementsTodaySum,
        mostRecentIncrement,
        mostRecentIncrementInstant)
}

