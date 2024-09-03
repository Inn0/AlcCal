package nl.daanbrocatus.alccal.screens.stats

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import nl.daanbrocatus.alccal.composables.common.DropdownSelector

@Composable
fun StatsScreen(viewModel: StatsScreenViewModel) {
    val dateTimeList by viewModel.filteredDateTimes.collectAsState(emptyList())
    val timestampsPerDay by viewModel.timestampsPerDay.collectAsState()
    val selectedYear by viewModel.selectedYear.collectAsState()
    val selectedMonth by viewModel.selectedMonth.collectAsState()
    val years = (2024..2030).toList()
    val months = (1..12).toList()

    LaunchedEffect(dateTimeList) {
        Log.e("StatsScreen", "dateTimeList updated: $dateTimeList")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Text(
            text = "Statistics",
            fontStyle = MaterialTheme.typography.displayMedium.fontStyle,
            fontSize = MaterialTheme.typography.displayMedium.fontSize,
            fontWeight = MaterialTheme.typography.displayMedium.fontWeight,
            modifier = Modifier.padding(8.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DropdownSelector(
                label = "Month",
                selectedItem = selectedMonth,
                items = months,
                onItemSelected = { month -> viewModel.setSelectedMonth(month) }
            )

            DropdownSelector(
                label = "Year",
                selectedItem = selectedYear,
                items = years,
                onItemSelected = { year -> viewModel.setSelectedYear(year) }
            )
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(timestampsPerDay.keys.sorted()) { day ->
                val count = timestampsPerDay[day] ?: 0
                DayCard(day = day, count = count)
            }
        }
    }
}

@Composable
fun DayCard(day: Int, count: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier
        ) {
            Card(
                colors = CardDefaults.cardColors()
                    .copy(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .size(48.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = day.toString(),
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }

            Text(
                text = count.toString(),
                modifier = Modifier
            )
        }
    }
}