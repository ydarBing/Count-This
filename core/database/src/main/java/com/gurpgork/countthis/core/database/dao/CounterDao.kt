package com.gurpgork.countthis.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.gurpgork.countthis.core.database.model.CounterEntity
import com.gurpgork.countthis.core.database.model.IncrementEntity
import com.gurpgork.countthis.core.database.resultentities.CounterWithIncrementInfoEntity
import com.gurpgork.countthis.core.database.resultentities.CounterWithIncrementsAndHistoryEntity
import com.gurpgork.countthis.core.model.data.SortOption
import kotlinx.coroutines.flow.Flow

@Dao
interface CounterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: CounterEntity): Long

    @Update
    suspend fun update(entity: CounterEntity)
    @Upsert
    suspend fun upsert(entity: CounterEntity)

    @Query("UPDATE counters SET list_index = :newIndex WHERE id = :counterId")
    suspend fun updateListIndex(counterId: Long, newIndex: Int): Int
    @Query("UPDATE counters SET count = :newCount WHERE id = :counterId")
    suspend fun updateCount(counterId: Long, newCount: Int): Int

    @Query("SELECT * FROM counters")
    fun countersObservable(): Flow<List<CounterEntity>>

    @Query("SELECT * FROM counters LIMIT :count")
    fun countersObservable(count: Int): Flow<List<CounterEntity>>

    fun observeSqlSortedCountersWithInfo(sort: SortOption): Flow<List<CounterWithIncrementInfoEntity>>{
        // adding a string to orderBy essentially just makes it a string: "sort.sqlValue" when we
        // want actual column: sort.sqlValue
        //return countersSorted(sort.sqlValue)

        val ascOrDesc: String = when(sort){
            SortOption.USER_SORTED,
            SortOption.ALPHABETICAL -> " ASC"

            SortOption.LAST_UPDATED,
            SortOption.DATE_ADDED -> " DESC"
        }
        val sqlValue = sort.sqlValue
        val finalQuery = StringBuilder(ENTRY_QUERY_ALL_COUNTERS)
        finalQuery.append(" ORDER BY $sqlValue $ascOrDesc")
        val simpleSQLiteQuery = SimpleSQLiteQuery(finalQuery.toString())
        return countersSortedRawQuery(simpleSQLiteQuery)
    }

    @Transaction
    @RawQuery(observedEntities = [CounterEntity::class, IncrementEntity::class] )
    fun countersSortedRawQuery(query: SupportSQLiteQuery) : Flow<List<CounterWithIncrementInfoEntity>>

    @Transaction
    @Query(ENTRY_QUERY_ALL_COUNTERS)
    fun observeCountersWithInfo(): Flow<List<CounterWithIncrementInfoEntity>>


    fun observePagedList(): PagingSource<Int, CounterWithIncrementInfoEntity>{
      return pagedListSorted()
    }

    @Transaction
    @Query(ENTRY_QUERY_ALL_COUNTERS)
    fun pagedListSorted(): PagingSource<Int, CounterWithIncrementInfoEntity>

    @Query("SELECT * FROM counters WHERE id = :counterId")
    fun observeCounter(counterId: Long): Flow<CounterEntity>

    @Query("SELECT * FROM counters WHERE id = :counterId")
    suspend fun getCounterById(counterId: Long): CounterEntity?

    @Transaction
    @Query("SELECT * FROM counters WHERE id IN (:counterId)")
    fun observeCounterWithIncrementsAndHistory(counterId: Long) : Flow<CounterWithIncrementsAndHistoryEntity?>

    @Transaction
    @Query("SELECT * FROM counters WHERE id IN (:counterId)")
    suspend fun getCounterWithIncrementsAndHistory(counterId: Long) : CounterWithIncrementsAndHistoryEntity?

    @Delete
    suspend fun delete(counter: CounterEntity)

    @Query("DELETE FROM counters WHERE id = :counterId")
    suspend fun deleteWithId(counterId: Long)

    @Query("UPDATE counters SET list_index = list_index - 1 where list_index > :listIndex")
    suspend fun updateListIndices(listIndex: Int): Int

    @Transaction
    suspend fun deleteAndUpdateListIndices(counterId: Long, listIndex: Int){
        deleteWithId(counterId)
        updateListIndices(listIndex)
    }

    @Query("DELETE FROM counters")
    suspend fun deleteAll()

    @Query("UPDATE counters SET count = 0 WHERE id = :counterId")
    suspend fun resetCounter(counterId: Long): Int



    companion object {
        private const val ENTRY_QUERY_ALL_COUNTERS = """
            SELECT c.*, 
	   coalesce(todays_increments.sit, 0) as increments_today_sum,
	   coalesce(todays_increments.cit, 0) as increments_today_count,
	   coalesce(todays_increments.mri, c.creation_date) as most_recent_increment_date
FROM counters AS c
LEFT JOIN
	(
	SELECT 
		i.counter_id,
		COUNT(CASE WHEN DATE(i.increment_date/1000,'unixepoch','localtime') = DATE('now','localtime') THEN i.increment END) AS cit,
        SUM(CASE WHEN DATE(i.increment_date/1000,'unixepoch','localtime') = DATE('now','localtime') THEN i.increment END) AS sit,
		MAX(i.increment_date) AS mri
	FROM increments as i 
	GROUP BY i.counter_id
	) AS todays_increments ON c.id = todays_increments.counter_id 
        """
    }
    // TODO when minsdk is increased to 31+ we can use FILTER
    // SUM(i.increment) FILTER( WHERE DATE(i.increment_date/1000,'unixepoch','localtime') = DATE('now','localtime')) as sit
}