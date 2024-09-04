package nl.daanbrocatus.alccal.screens.stats

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import nl.daanbrocatus.alccal.R
import nl.daanbrocatus.alccal.composables.common.DropdownSelector

@Composable
fun StatsScreen(viewModel: StatsScreenViewModel) {
    val timestampsPerDay by viewModel.timestampsPerDay.collectAsState()
    val selectedYear by viewModel.selectedYear.collectAsState()
    val selectedMonth by viewModel.selectedMonth.collectAsState()
    val years = (2024..2030).toList()
    val months = (1..12).toList()

    val monthNames = listOf(
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 0.dp)
    ) {
        Text(
            text = monthNames[selectedMonth - 1],
            fontStyle = MaterialTheme.typography.displayMedium.fontStyle,
            fontSize = MaterialTheme.typography.displayMedium.fontSize,
            fontWeight = MaterialTheme.typography.displayMedium.fontWeight,
            modifier = Modifier.padding(8.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
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
            item {
                Spacer(modifier = Modifier.fillMaxWidth().height(4.dp))
            }
        }
    }
}

@Composable
fun DayCard(day: Int, count: Int) {
    val heatMapColorCodes = listOf(
        0xFF00FF00,
        0xFF55FF00,
        0xFFAAFF00,
        0xFFFFEA00,
        0xFFFF9500,
        0xFFFF4000,
        0xFFFF2A00,
        0xFFFF0000
    )

    val targetColor: Color = when (count) {
        0 -> MaterialTheme.colorScheme.primary
        in 1..8 -> Color(heatMapColorCodes[count - 1])
        else -> Color(heatMapColorCodes.last())
    }
    val animatedColor by animateColorAsState(targetColor, label = "color")
    val animatedCount by animateIntAsState(count, label = "count")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Card(
                colors = CardDefaults.cardColors()
                    .copy(containerColor = animatedColor),
                modifier = Modifier
                    .size(48.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = day.toString(),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = animatedCount.toString(),
                    modifier = Modifier.padding(start = 16.dp, end = 8.dp),
                    fontSize = MaterialTheme.typography.titleLarge.fontSize
                )
                Icon(
                    painter = painterResource(id = if (count > 0) R.drawable.baseline_sports_bar_24 else R.drawable.outline_sports_bar_24),
                    contentDescription = "Drinks icon",
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
