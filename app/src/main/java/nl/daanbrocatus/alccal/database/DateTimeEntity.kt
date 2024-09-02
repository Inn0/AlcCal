package nl.daanbrocatus.alccal.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "datetime_table")
data class DateTimeEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as DateTimeEntity
        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}