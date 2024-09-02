package nl.daanbrocatus.alccal.database

import kotlinx.coroutines.flow.Flow

class DateTimeRepository(private val dateTimeDao: DateTimeDao) {
    suspend fun insertDateTime(timestamp: String) {
        dateTimeDao.insert(DateTimeEntity(timestamp = timestamp))
    }

    suspend fun getAllDateTimes(): Flow<List<DateTimeEntity>> {
        return dateTimeDao.getAll()
    }

    suspend fun deleteDateTime(dateTimeEntity: DateTimeEntity) {
        dateTimeDao.delete(dateTimeEntity)
    }
}
