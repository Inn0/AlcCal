package nl.daanbrocatus.alccal.screens.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import nl.daanbrocatus.alccal.database.DateTimeEntity
import nl.daanbrocatus.alccal.database.DateTimeRepository
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class StatsScreenViewModel(private val repository: DateTimeRepository) : ViewModel() {

    private val _allDateTimes = MutableStateFlow<List<DateTimeEntity>>(emptyList())
    val allDateTimes: StateFlow<List<DateTimeEntity>> = _allDateTimes.asStateFlow()

    private val _selectedYear = MutableStateFlow(LocalDate.now().year)
    val selectedYear: StateFlow<Int> = _selectedYear.asStateFlow()

    private val _selectedMonth = MutableStateFlow(LocalDate.now().monthValue)
    val selectedMonth: StateFlow<Int> = _selectedMonth.asStateFlow()

    val filteredDateTimes: StateFlow<List<DateTimeEntity>> = combine(
        _allDateTimes,
        _selectedYear,
        _selectedMonth
    ) { dateTimes, selectedYear, selectedMonth ->
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        dateTimes.filter { dateTime ->
            val localDate = LocalDate.parse(dateTime.timestamp, formatter)
            localDate.year == selectedYear && localDate.monthValue == selectedMonth
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        emptyList()
    )

    val timestampsPerDay = combine(_allDateTimes, selectedYear, selectedMonth) { dateTimes, year, month ->
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
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
}