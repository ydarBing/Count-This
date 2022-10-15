package com.gurpgork.countthis.counter

import androidx.annotation.WorkerThread
import androidx.paging.PagingSource
import com.gurpgork.countthis.data.daos.CounterDao
import com.gurpgork.countthis.data.daos.HistoryDao
import com.gurpgork.countthis.data.daos.IncrementDao
import com.gurpgork.countthis.data.entities.CounterEntity
import com.gurpgork.countthis.data.resultentities.CounterWithIncrementInfo
import com.gurpgork.countthis.util.SortOption
import kotlinx.coroutines.flow.Flow
import java.time.OffsetDateTime
import javax.inject.Inject

//Repository implements the logic for deciding whether to fetch data from a network
//  or use results cached in a local database

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class CounterRepository @Inject constructor (
    private val counterDao: CounterDao,
    private val incrementDao: IncrementDao,
    private val historyDao: HistoryDao
    ) {
    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allCounters: Flow<List<CounterEntity>> = counterDao.countersObservable()

    suspend fun resetCounter(counterId: Long) {
        val counter = counterDao.getCounter(counterId)
        val lastReset = historyDao.getMostRecentReset(counterId)
        if (counter != null) {
            historyDao.insert(
                HistoryEntity(
                    counterId = counterId,
                    count = counter.count,
                start_date = if(lastReset != null) lastReset.end_date else counter.creation_date_time,
                end_date = OffsetDateTime.now()))
        }
        incrementDao.deleteAllFromCounterId(counterId)

        counterDao.resetCounter(counterId)
    }
    suspend fun deleteWithId(id: Long) = counterDao.deleteWithId(id)

    suspend fun getCounter(id: Long) = counterDao.getCounter(id)

    // TODO make this a paging if there's lots of counters
    fun observeCounterList(numCounterToObserver: Int) = counterDao.countersObservable(numCounterToObserver)

    fun observeForPaging(sort: SortOption): PagingSource<Int, CounterWithIncrementInfo> {
        return when(sort){
            SortOption.ALPHABETICAL -> {
                counterDao.pagedListAlphabetical()
            }
            // TODO uncomment once working
//            SortOption.DATE_ADDED -> {
//                counterDao.pagedListDateAdded()
//            }
//            SortOption.LAST_UPDATED -> {
//                counterDao.pagedListLastUpdated()
//            }
//            SortOption.USER_SORTED -> {
//                counterDao.pagedListUserSorted()
//            }
            else -> throw IllegalArgumentException("$sort option is not supported")
        }
    }//= counterDao.observePagedList(sort)



    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertCounter(counter: CounterEntity) {
        counterDao.insert(counter)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateCounter(counter: CounterEntity) {
//        counterDao.insertOrUpdate(counter)
        counterDao.update(counter)
    }
}