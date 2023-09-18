package com.gurpgork.countthis.core.database.resultentities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import com.gurpgork.countthis.core.database.model.CounterEntity
import kotlinx.datetime.Instant
import java.util.Objects

class CounterWithIncrementInfoEntity{
    @Embedded
    lateinit var counter: CounterEntity

    @ColumnInfo(name = "increments_today_count")
    var incrementsTodayCount: Long = 0
    @ColumnInfo(name = "increments_today_sum")
    var incrementsTodaySum: Long = 0
    @ColumnInfo(name = "most_recent_increment_date")
    var mostRecentIncrementDate: Instant? = null

    /**
     * Allow consumers to destructure this class
     */
    operator fun component1() = counter
    operator fun component2() = incrementsTodayCount
    operator fun component3() = incrementsTodaySum
    operator fun component4() = mostRecentIncrementDate


    override fun equals(other: Any?): Boolean = when {
        other === this -> true
        other is CounterWithIncrementInfoEntity -> {
            counter == other.counter &&
                    incrementsTodayCount == other.incrementsTodayCount &&
                    incrementsTodaySum == other.incrementsTodaySum &&
                    mostRecentIncrementDate == other.mostRecentIncrementDate
        }
        else -> false
    }

    override fun hashCode(): Int = Objects.hash(counter,
        incrementsTodayCount,
        incrementsTodaySum,
        mostRecentIncrementDate)
}

