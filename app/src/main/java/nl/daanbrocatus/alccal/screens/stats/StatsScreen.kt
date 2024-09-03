package nl.daanbrocatus.alccal.screens.stats

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StatsScreen(viewModel: StatsScreenViewModel) {
    val dateTimeList by viewModel.filteredDateTimes.collectAsState(emptyList())
    val selectedYear by viewModel.selectedYear.collectAsState()
    val selectedMonth by viewModel.selectedMonth.collectAsState()
    val years = (2024..2030).toList()
    val months = (1..12).toList()

    var isMonthDropdownExpanded by remember { mutableStateOf(false) }
    var isYearDropdownExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(dateTimeList) {
        Log.e("StatsScreen", "dateTimeList updated: $dateTimeList")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DropdownSelector(
                label = "Select Month",
                selectedItem = selectedMonth,
                items = months,
                onItemSelected = { month -> viewModel.setSelectedMonth(month) }
            )

            DropdownSelector(
                label = "Select Year",
                selectedItem = selectedYear,
                items = years,
                onItemSelected = { year -> viewModel.setSelectedYear(year) }
            )
        }

        Column(modifier = Modifier.fillMaxSize()) {
            dateTimeList.forEach { dateTimeEntity ->
                Text(text = dateTimeEntity.timestamp)
            }
        }
    }
}

@Composable
fun <T> DropdownSelector(
    label: String,
    selectedItem: T,
    items: List<T>,
    onItemSelected: (T) -> Unit
) {
    var isDropdownExpanded by remember { mutableStateOf(false) }

    Box {
        Text(
            text = "$label: $selectedItem",
            modifier = Modifier
                .padding(8.dp)
                .clickable { isDropdownExpanded = true }
        )

        DropdownMenu(
            expanded = isDropdownExpanded,
            onDismissRequest = { isDropdownExpanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = item.toString()) },
                    onClick = {
                        onItemSelected(item)
                        isDropdownExpanded = false
                    }
                )
            }
        }
    }
}

