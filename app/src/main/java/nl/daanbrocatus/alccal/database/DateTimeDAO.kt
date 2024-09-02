package nl.daanbrocatus.alccal.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DateTimeDao {
    @Insert
    suspend fun insert(dateTimeEntity: DateTimeEntity)
    @Query("SELECT * FROM datetime_table")
    fun getAll(): Flow<List<DateTimeEntity>>
    @Delete
    suspend fun delete(dateTimeEntity: DateTimeEntity)
}