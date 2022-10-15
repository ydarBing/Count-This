package com.gurpgork.countthis.data

import com.gurpgork.countthis.data.daos.CounterDao
import com.gurpgork.countthis.data.daos.HistoryDao
import com.gurpgork.countthis.data.daos.IncrementDao
import com.gurpgork.countthis.data.daos.LocationDao

interface CountThisDatabase {
    fun counterDao(): CounterDao
    fun historyDao(): HistoryDao
    fun incrementDao(): IncrementDao
    fun locationDao(): LocationDao
}