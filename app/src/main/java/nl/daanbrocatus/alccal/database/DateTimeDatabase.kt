package nl.daanbrocatus.alccal.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DateTimeEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dateTimeDao(): DateTimeDao
}