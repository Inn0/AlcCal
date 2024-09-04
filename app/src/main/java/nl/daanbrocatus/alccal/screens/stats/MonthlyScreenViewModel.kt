package nl.daanbrocatus.alccal.screens.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import nl.daanbrocatus.alccal.database.DateTimeEntity
import nl.daanbrocatus.alccal.database.DateTimeRepository
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class MonthlyScreenViewModel(private val repository: DateTimeRepository) : ViewModel() {

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    private val _allDateTimes = MutableStateFlow<List<DateTimeEntity>>(emptyList())

    private val _selectedYear = MutableStateFlow(LocalDate.now().year)
    val selectedYear: StateFlow<Int> = _selectedYear.asStateFlow()

    private val _selectedMonth = MutableStateFlow(LocalDate.now().monthValue)
    val selectedMonth: StateFlow<Int> = _selectedMonth.asStateFlow()

    val timestampsPerDay = combine(_allDateTimes, selectedYear, selectedMonth) { dateTimes, year, month ->
        val daysInMonth = YearMonth.of(year, month).lengthOfMonth()

        val dateCounts = (1..daysInMonth).associateWith { 0 }.toMutableMap()

        dateTimes.forEach { dateTime ->
            val localDate = LocalDate.parse(dateTime.timestamp, formatter)
            if (localDate.year == year && localDate.monthValue == month) {
                val day = localDate.dayOfMonth
                dateCounts[day] = dateCounts.getOrDefault(day, 0) + 1
            }
        }

        dateCounts.toMap()
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyMap())

    init {
        viewModelScope.launch {
            repository.getAllDateTimes().collect { dateTimes ->
                _allDateTimes.value = dateTimes
            }
        }
    }

    fun setSelectedYear(year: Int) {
        _selectedYear.value = year
    }

    fun setSelectedMonth(month: Int) {
        _selectedMonth.value = month
    }

    fun addDateTimeToDay(day: Int) {
        val dateTime = LocalDate.of(_selectedYear.value, _selectedMonth.value, day).atStartOfDay()
        val timestamp = dateTime.format(formatter)

        viewModelScope.launch {
            repository.insertDateTime(timestamp)
        }
    }

    fun deleteDateTimeFromDay(day: Int) {
        val year = _selectedYear.value
        val month = _selectedMonth.value
        val localDateToRemove = LocalDate.of(year, month, day)

        viewModelScope.launch {
            // Find the first matching DateTimeEntity for the given day
            val dateTimeToRemove = _allDateTimes.value
                .firstOrNull { dateTime ->
                    LocalDate.parse(dateTime.timestamp, formatter).let { date ->
                        date.year == year && date.monthValue == month && date.dayOfMonth == day
                    }
                }

            // If a matching DateTimeEntity is found, delete it
            dateTimeToRemove?.let { dateTime ->
                repository.deleteDateTime(dateTime)
            }
        }
    }
}