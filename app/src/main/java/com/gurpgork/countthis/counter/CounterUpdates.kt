package com.gurpgork.countthis.counter

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import java.util.*

class CounterUpdates {
    inner class IncrementInfo : ClusterItem {
        internal constructor(time_: Long, inc: Int) {
            time = time_
            increment = inc
            altitude = 0.0
            accuracy = 0f
            position = LatLng(0.0, 0.0)
        }

        internal constructor(time_: Long, inc: Int, loc: Location) {
            time = time_
            increment = inc
            position = LatLng(loc.latitude, loc.longitude)
            altitude = loc.altitude
            accuracy = loc.accuracy
        }

        override fun getPosition(): LatLng {
            return position
        }

        override fun getTitle(): String? {
            TODO("Not yet implemented")
        }

        override fun getSnippet(): String? {
            TODO("Not yet implemented")
        }
        var time: Long
        private var position: LatLng
        var altitude: Double
        var accuracy: Float

        //public float  speed;
        var increment: Int
        var markerTitle: String? = null
        var markerSnippet: String? = null
    }

    inner class TimeKeeper internal constructor(var mDay: Long) {
        var mOneDay: Vector<IncrementInfo> = Vector()

    }

    private val mTimes: Vector<TimeKeeper> = Vector()
    private var mAwaitingUpdate: Boolean = false
    private var mAwaitingStartDayIndex: Int = 0
    private var mAwaitingStartTimeIndex: Int = 0


    fun GetTimes(): Vector<TimeKeeper> {
        return mTimes
    }

    fun GetAwaitingStartDayIndex(): Int {
        return mAwaitingStartDayIndex
    }

    fun GetAwaitingStartTimeIndex(): Int {
        return mAwaitingStartTimeIndex
    }

    fun GetLastUpdateIndex(): Int {
        return mTimes.lastElement().mOneDay.size - 1
    }

    // should only be removing from stats
    // assumed that dateIndex is going need to be starting from end of array
    // returns accumulated increments i.e if increment was 20, will return -20
    fun RemoveTime(dateIndex: Int, timeIndex: Int): RemovedTimeInfo {
        val endIndex = mTimes.size - 1
        if (mTimes[endIndex - dateIndex].mOneDay.size == 1) // the last time in this day
        {
            return RemoveDate(dateIndex)
        }
        val ret = RemovedTimeInfo(1)
        ret.incrementsRemoved = (-mTimes[endIndex - dateIndex].mOneDay[timeIndex].increment).toLong()
        ret.times[0] = mTimes[endIndex - dateIndex].mOneDay[timeIndex].time
        // actually remove time from times
        mTimes[endIndex - dateIndex].mOneDay.removeAt(timeIndex)
        return ret
    }

    // should only be removing from stats
    // assumed that dateIndex is going need to be starting from end of array
    // returns accumulated increments i.e if increment was 20, will return -20
    fun RemoveDate(dateIndex: Int): RemovedTimeInfo {
        val endIndex = mTimes.size - 1
        val numToRemove = mTimes[endIndex - dateIndex].mOneDay.size
        val toRemove = mTimes[endIndex - dateIndex]
        val ret = RemovedTimeInfo(toRemove.mOneDay.size)
        for (i in 0 until numToRemove) {
            ret.incrementsRemoved -= toRemove.mOneDay[i].increment
            ret.times[i] = toRemove.mOneDay[i].time
        }
        mTimes.removeAt(endIndex - dateIndex) // actually remove from records
        return ret
    }

    fun Clear() {
        mTimes.clear()
    }

    fun GetMostRecentUpdateTime(): Long {
        return if (mTimes.isEmpty()) {
            -1
        } else {
            mTimes.lastElement().mOneDay.lastElement().time
        }
    }

    fun GetMostRecentUpdate(): IncrementInfo? {
        return if (mTimes.isEmpty()) {
            null
        } else {
            mTimes.lastElement().mOneDay.lastElement()
        }
    }

    fun AddTime(dayTime: Long, exactTime: Long, increment: Int, location: Location?) {
        if (mTimes.isEmpty() || mTimes.lastElement().mDay != dayTime) mTimes.add(TimeKeeper(dayTime))
        if (location != null) mTimes.lastElement().mOneDay.add(
            IncrementInfo(
                exactTime,
                increment,
                location
            )
        ) else mTimes.lastElement().mOneDay.add(IncrementInfo(exactTime, increment))
    }

    fun SetAwaitingUpdate(update: Boolean) {
        // check if this is the first update
        if (!mAwaitingUpdate && update) {
            // since the date would have already been added, we want to get the last element
            // as the start index for updating
            mAwaitingStartDayIndex = mTimes.size - 1
            mAwaitingStartTimeIndex = mTimes.lastElement().mOneDay.size - 1
        }
        mAwaitingUpdate = update
    }

    fun GetAwaitingUpdate(): Boolean {
        return mAwaitingUpdate
    }
}