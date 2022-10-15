package com.gurpgork.countthis.inject

import androidx.room.withTransaction
import com.gurpgork.countthis.CountThisRoomDatabase
import com.gurpgork.countthis.data.DatabaseTransactionRunner
import javax.inject.Inject

class RoomTransactionRunner @Inject constructor(
    private val db: CountThisRoomDatabase
) : DatabaseTransactionRunner {
    override suspend operator fun <T> invoke(block: suspend () -> T): T {
        return db.withTransaction {
            block()
        }
    }
}