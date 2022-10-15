package com.gurpgork.countthis.counter

class RemovedTimeInfo internal constructor(numTimes: Int) {
    var times: LongArray = LongArray(numTimes)
    var incrementsRemoved: Long = 0
    var updateNewestTime: Boolean = false
}