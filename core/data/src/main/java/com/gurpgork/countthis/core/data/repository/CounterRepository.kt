package com.gurpgork.countthis.core.data.repository

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.gurpgork.countthis.core.data.model.CounterWithIncrementInfo
import com.gurpgork.countthis.core.data.model.CounterWithIncrementsAndHistory
import com.gurpgork.countthis.core.model.data.Counter
import com.gurpgork.countthis.core.model.data.SortOption
import kotlinx.coroutines.flow.Flow

//Repository implements the logic for deciding whether to fetch data from a network
//  or use results cached in a local database

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
interface CounterRepository {
    suspend fun resetCounter(counterId: Long)
    suspend fun deleteWithId(id: Long)
    suspend fun getCounterById(id: Long): Counter?
    fun observeCounter(id: Long): Flow<Counter>
    fun observeCounterList(numCounterToObserver: Int): Flow<List<Counter>>
    fun observeCountersWithInfo(): Flow<List<CounterWithIncrementInfo>>

    fun observeCountersWithInfo(sqlSort: SortOption): Flow<List<CounterWithIncrementInfo>>
    fun observeForPaging(
        pagingConfig: PagingConfig,
        sort: SortOption
    ): Flow<PagingData<CounterWithIncrementInfo>>

    fun observeCounterWithIncrementsAndHistory(counterId: Long): Flow<CounterWithIncrementsAndHistory?>

    suspend fun getCounterWithIncrementsAndHistory(counterId: Long): CounterWithIncrementsAndHistory?

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    suspend fun insertCounter(counter: Counter)
    suspend fun updateCounter(counter: Counter)


}