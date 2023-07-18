package com.gurpgork.countthis.core.data.repository

import androidx.annotation.WorkerThread
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.gurpgork.countthis.core.data.model.CounterWithIncrementInfo
import com.gurpgork.countthis.core.data.model.CounterWithIncrementsAndHistory
import com.gurpgork.countthis.core.data.model.asExternalModel
import com.gurpgork.countthis.core.database.dao.CounterDao
import com.gurpgork.countthis.core.database.dao.HistoryDao
import com.gurpgork.countthis.core.database.dao.IncrementDao
import com.gurpgork.countthis.core.database.model.CounterEntity
import com.gurpgork.countthis.core.database.model.HistoryEntity
import com.gurpgork.countthis.core.database.model.asExternalModel
import com.gurpgork.countthis.core.model.data.Counter
import com.gurpgork.countthis.core.model.data.SortOption
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.OffsetDateTime
import javax.inject.Inject

//Repository implements the logic for deciding whether to fetch data from a network
//  or use results cached in a local database

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class DefaultCounterRepository @Inject constructor(
    private val counterDao: CounterDao,
    private val incrementDao: IncrementDao,
    private val historyDao: HistoryDao,
) : CounterRepository {
    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allCounters: Flow<List<CounterEntity>> = counterDao.countersObservable()

    override suspend fun resetCounter(counterId: Long) {
        val counter = counterDao.getCounter(counterId)
        val lastReset = historyDao.getMostRecentReset(counterId)

        historyDao.insert(
            HistoryEntity(
                counterId = counterId,
                count = counter.count,
                startDate = lastReset?.endDate ?: counter.creationDateTime,
                endDate = OffsetDateTime.now()
            )
        )
        incrementDao.deleteAllFromCounterId(counterId)

        counterDao.resetCounter(counterId)
    }

    override suspend fun deleteWithId(id: Long) = counterDao.deleteWithId(id)

    override fun getCounter(id: Long) =
        counterDao.getCounter(id).asExternalModel()

    override fun observeCounter(id: Long) =
        counterDao.observeCounter(id).map { it.asExternalModel() }

    override fun observeCounterList(numCounterToObserver: Int) =
        counterDao.countersObservable(numCounterToObserver)
            .map { it.map(CounterEntity::asExternalModel) }

    override fun observeForPaging(
        pagingConfig: PagingConfig,
        sort: SortOption
    ): Flow<PagingData<CounterWithIncrementInfo>> {
        return when (sort) {
            SortOption.ALPHABETICAL -> {
                Pager(config = pagingConfig) { counterDao.pagedListAlphabetical() }
                    .flow
                    .map { pagingData ->
                        pagingData.map {
                            it.asExternalModel()
                        }
                    }
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
    }

    //    override fun observeForPaging(sort: SortOption): PagingSource<Int, CounterWithIncrementInfoEntity> {
//        return when (sort) {
//            SortOption.ALPHABETICAL -> {
//                counterDao.pagedListAlphabetical()
//            }
//            // TODO uncomment once working
////            SortOption.DATE_ADDED -> {
////                counterDao.pagedListDateAdded()
////            }
////            SortOption.LAST_UPDATED -> {
////                counterDao.pagedListLastUpdated()
////            }
////            SortOption.USER_SORTED -> {
////                counterDao.pagedListUserSorted()
////            }
//            else -> throw IllegalArgumentException("$sort option is not supported")
//        }
//    }//= counterDao.observePagedList(sort)
    override fun observeCounterWithIncrementsAndHistory(counterId: Long): Flow<CounterWithIncrementsAndHistory?> =
        counterDao.observeCounterWithIncrementsAndHistory(counterId).map { it?.asExternalModel() }


    override suspend fun getCounterWithIncrementsAndHistory(counterId: Long): CounterWithIncrementsAndHistory? =
        counterDao.getCounterWithIncrementsAndHistory(counterId)?.asExternalModel()


    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @WorkerThread
    override suspend fun insertCounter(counter: Counter) {
        counterDao.insert(CounterEntity(counter))
    }

    @WorkerThread
    override suspend fun updateCounter(counter: Counter) {
//        counterDao.insertOrUpdate(counter)
        counterDao.update(CounterEntity(counter))
    }
}
