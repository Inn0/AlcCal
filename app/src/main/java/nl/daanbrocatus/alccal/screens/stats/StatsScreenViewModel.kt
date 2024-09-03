package nl.daanbrocatus.alccal.screens.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import nl.daanbrocatus.alccal.database.DateTimeEntity
import nl.daanbrocatus.alccal.database.DateTimeRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class StatsScreenViewModel(private val repository: DateTimeRepository) : ViewModel() {

    private val _allDateTimes = MutableStateFlow<List<DateTimeEntity>>(emptyList())
    val allDateTimes: StateFlow<List<DateTimeEntity>> = _allDateTimes.asStateFlow()

    private val _selectedYear = MutableStateFlow(LocalDate.now().year)
    val selectedYear: StateFlow<Int> = _selectedYear.asStateFlow()

    private val _selectedMonth = MutableStateFlow(LocalDate.now().monthValue)
    val selectedMonth: StateFlow<Int> = _selectedMonth.asStateFlow()

    val filteredDateTimes = _allDateTimes.map { dateTimes ->
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        dateTimes.filter { dateTime ->
            val localDate = LocalDate.parse(dateTime.timestamp, formatter)
            localDate.year == _selectedYear.value && localDate.monthValue == _selectedMonth.value
        }
    }

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