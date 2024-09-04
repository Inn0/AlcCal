package nl.daanbrocatus.alccal.screens.stats

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import nl.daanbrocatus.alccal.R
import nl.daanbrocatus.alccal.composables.common.DropdownSelector

@Composable
fun MonthlyScreen(viewModel: MonthlyScreenViewModel) {
    val timestampsPerDay by viewModel.timestampsPerDay.collectAsState()
    val selectedYear by viewModel.selectedYear.collectAsState()
    val selectedMonth by viewModel.selectedMonth.collectAsState()
    val years = (2024..2030).toList()
    val months = (1..12).toList()

    val isEditing = rememberSaveable { mutableStateOf(false) }

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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = monthNames[selectedMonth - 1],
                fontStyle = MaterialTheme.typography.displayMedium.fontStyle,
                fontSize = MaterialTheme.typography.displayMedium.fontSize,
                fontWeight = MaterialTheme.typography.displayMedium.fontWeight,
                modifier = Modifier.padding(8.dp)
            )

            Icon(
                painter = painterResource(id = if (isEditing.value) R.drawable.baseline_edit_off_24 else R.drawable.baseline_edit_24),
                contentDescription = "Edit icon",
                modifier = Modifier
                    .size(48.dp)
                    .padding(4.dp)
                    .clickable {
                        isEditing.value = !isEditing.value
                    },
                tint = MaterialTheme.colorScheme.primary
            )
        }

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
                DayCard(
                    day = day,
                    count = count,
                    onPlus = { viewModel.addDateTimeToDay(day) },
                    onMinus = { viewModel.deleteDateTimeFromDay(day) },
                    isEditing = isEditing.value
                )
            }
            item {
                Spacer(modifier = Modifier.fillMaxWidth().height(4.dp))
            }
        }
    }
}

@Composable
fun DayCard(day: Int, count: Int, onPlus: () -> Unit, onMinus: () -> Unit, isEditing: Boolean) {
    val heatMapColorCodes = listOf(
        0xFF007F00,
        0xFF4C8C00,
        0xFF8C8C00,
        0xFFC7C700,
        0xFFC77F00,
        0xFFC73D00,
        0xFFC72C00,
        0xFFC70000
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
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
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

            if (isEditing) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_add_24),
                        contentDescription = "Plus icon",
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .clickable { onPlus() }
                            .size(24.dp),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_remove_24),
                        contentDescription = "Minus icon",
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .clickable { onMinus() }
                            .size(24.dp),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}
