package nl.daanbrocatus.alccal.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import nl.daanbrocatus.alccal.database.DateTimeEntity
import nl.daanbrocatus.alccal.database.DateTimeRepository
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class HomeScreenViewModel(private val repository: DateTimeRepository) : ViewModel() {
    private val _allDateTimes = MutableStateFlow<List<DateTimeEntity>>(emptyList())
    val allDateTimes: StateFlow<List<DateTimeEntity>> get() = _allDateTimes

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> get() = _toastMessage

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    init {
        viewModelScope.launch {
            repository.getAllDateTimes()
                .map { dateTimes ->
                    dateTimes.sortedByDescending { dateTimeEntity ->
                        val timestamp = dateTimeEntity.timestamp
                        LocalDateTime.parse(timestamp, formatter).atZone(ZoneId.systemDefault()).toInstant()
                    }
                }
                .collect { sortedDateTimes ->
                    _allDateTimes.value = sortedDateTimes
                }
        }
    }

    fun insertDateTime() {
        viewModelScope.launch {
            repository.insertDateTime(getCurrentUtcDateTimeAsString())
        }
    }

    fun deleteDateTime(dateTimeEntity: DateTimeEntity) {
        viewModelScope.launch {
            repository.deleteDateTime(dateTimeEntity)
            _toastMessage.value = "Drink from ${dateTimeEntity.timestamp.substring(0, 10)} deleted!"
        }
    }

    val last24HoursCount: StateFlow<Int> = _allDateTimes
        .map { dateTimes ->
            val now = Instant.now()
            val twentyFourHoursAgo = now.minus(24, ChronoUnit.HOURS)
            dateTimes.count { dateTimeEntity ->
                val timestamp = dateTimeEntity.timestamp
                val localDateTime = LocalDateTime.parse(timestamp, formatter)
                val dateTime = localDateTime.atZone(ZoneId.systemDefault()).toInstant()
                dateTime.isAfter(twentyFourHoursAgo) && dateTime.isBefore(now)
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, 0)

    fun clearToastMessage() {
        _toastMessage.value = null
    }

    private fun getCurrentUtcDateTimeAsString(): String {
        val now = Instant.now()
        return formatter.withZone(ZoneId.systemDefault()).format(now)
    }
}
