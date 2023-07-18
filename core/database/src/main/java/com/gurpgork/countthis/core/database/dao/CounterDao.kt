package com.gurpgork.countthis.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.gurpgork.countthis.core.database.model.CounterEntity
import com.gurpgork.countthis.core.database.resultentities.CounterWithIncrementInfoEntity
import com.gurpgork.countthis.core.database.resultentities.CounterWithIncrementsAndHistoryEntity
import com.gurpgork.countthis.core.model.data.SortOption
import kotlinx.coroutines.flow.Flow

@Dao
interface CounterDao {//: EntityDao<CounterEntity>() {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: CounterEntity): Long

    @Update
    suspend fun update(entity: CounterEntity)

    @Query("SELECT * FROM counters")
    fun countersObservable(): Flow<List<CounterEntity>>

    @Query("SELECT * FROM counters LIMIT :count")
    fun countersObservable(count: Int): Flow<List<CounterEntity>>

    //TODO add back in different sort options
    fun observePagedList(sort: SortOption): PagingSource<Int, CounterWithIncrementInfoEntity>{
      return pagedListAlphabetical()
//        when(sort){
//          SortOption.ALPHABETICAL -> {
//              pagedListAlphabetical()
//          }
//          SortOption.DATE_ADDED -> {
//              pagedListDateAdded()
//          }
//          SortOption.LAST_UPDATED -> {
//              pagedListLastUpdated()
//          }
//          SortOption.USER_SORTED -> {
//              pagedListUserSorted()
//          }
          //else -> throw IllegalArgumentException("$sort option is not supported")
//      }
    }
//    @Transaction
//    @Query("SELECT * FROM counters ORDER BY name ASC")
//    internal abstract fun pagedListAlphabetical(): PagingSource<Int, CounterEntity>

//    Only gets counters increments that happened today
//    @Transaction
//    @Query("SELECT counters.*, incs.*" +
//            " FROM counters" +
//            " INNER JOIN increments as incs ON counters.id = incs.counter_id" +
////            this doesn't involve wrapping date column in a function,
////            which means that SQLite may still use an index which might exist on date
//            " WHERE date >= DATE('now') AND date < DATE('now', '+1 day') " +
//            " ORDER BY name ASC, date ASC")
//    internal abstract fun pagedListAlphabeticals(): PagingSource<Int, CounterWithIncrements>


    @Transaction
    @Query(ENTRY_QUERY_ORDER_ALPHABETICAL)
    fun pagedListAlphabetical(): PagingSource<Int, CounterWithIncrementInfoEntity>
//    @Transaction
//    @Query(ENTRY_QUERY_ORDER_DATE_ADDED)
//    internal abstract fun pagedListDateAdded(): PagingSource<Int, CounterWithIncrementInfo>
//    @Transaction
//    @Query(ENTRY_QUERY_ORDER_LAST_UPDATED)
//    internal abstract fun pagedListLastUpdated(): PagingSource<Int, CounterWithIncrementInfo>
//    @Transaction
//    @Query(ENTRY_QUERY_ORDER_USER_SORTED)
//    internal abstract fun pagedListUserSorted(): PagingSource<Int, CounterWithIncrementInfo>





//    @Query("SELECT * FROM counters ORDER BY name ASC")
//    fun observeAlphabetizedCounters(): Flow<List<CounterEntity>>
//
    @Query("SELECT * FROM counters WHERE id = :counterId")
    fun observeCounter(counterId: Long): Flow<CounterEntity>

    @Query("SELECT * FROM counters WHERE id = :counterId")
    fun getCounter(counterId: Long): CounterEntity

//    @Query("SELECT * FROM counters WHERE name LIKE :name LIMIT 1")
//    suspend fun getByName(name: String): CounterEntity?

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

    @Query("DELETE FROM counters")
    suspend fun deleteAll()

    @Query("UPDATE counters SET count = 0 WHERE id = :counterId")
    suspend fun resetCounter(counterId: Long): Int

//    @Transaction
//    @Query("SELECT * FROM counters where id = :counterId")
//    fun observeCounterWithIncrements(counterId: Long): Flow<CounterWithIncrements>



    companion object {
        private const val ENTRY_QUERY_ORDER_ALPHABETICAL = """
            SELECT c.*, 
	   coalesce(todays_increments.sit, 0) as increments_today_sum,
	   coalesce(todays_increments.cit, 0) as increments_today_count,
	   coalesce(todays_increments.mri, c.created) as most_recent_increment,
	   todays_increments.mrii as most_recent_increment_instant,
       todays_increments.zoneOffset as most_recent_increment_instant_offset
FROM counters AS c
LEFT JOIN
	(
	SELECT 
		i.counter_id,
		SUM(i.increment) AS sit, 
		COUNT(i.increment) AS cit,
		MAX(i.date) AS mri,
		MAX(i.date_epoch) AS mrii,
        i.time_zone_offset as zoneOffset
	FROM increments as i 
	WHERE DATE(i.date_epoch / 1000, 'unixepoch', 'localtime') = DATE('now', 'localtime')
	GROUP BY i.counter_id
    ORDER BY i.date_epoch DESC
	) AS todays_increments ON c.id = todays_increments.counter_id
ORDER BY c.name
        """

        private const val ENTRY_QUERY_ORDER_DATE_ADDED =
            """
            SELECT c.*, 
	   coalesce(todays_increments.sit, 0) as increments_today_sum,
	   coalesce(todays_increments.cit, 0) as increments_today_count,
	   coalesce(todays_increments.mri, c.created) as most_recent_increment
FROM counters AS c
LEFT JOIN
	(
	SELECT 
		i.counter_id,
		SUM(i.increment) AS sit, 
		COUNT(i.increment) AS cit,
		MAX(i.date) AS mri
	FROM increments as i 
	WHERE DATE(i.date) = DATE('now')
	WHERE DATE(i.date) = DATE('now')
	GROUP BY i.counter_id
	ORDER BY datetime(i.date) ASC
	) AS todays_increments ON c.id = todays_increments.counter_id
ORDER BY c.creation_date
        """
//        private const val ENTRY_QUERY_ORDER_LAST_UPDATED =
//            """
//            SELECT * FROM counters as c
//            INNER JOIN increments as i ON c.id = i.counter_id
//            ORDER BY datetime(i.date) ASC
//        """
//        private const val ENTRY_QUERY_ORDER_USER_SORTED =
//            """
//            SELECT * FROM counters
//            ORDER BY list_index ASC
//        """
    }
}