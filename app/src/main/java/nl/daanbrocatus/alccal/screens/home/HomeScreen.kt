package nl.daanbrocatus.alccal.screens.home

import android.widget.Toast
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import nl.daanbrocatus.alccal.R
import nl.daanbrocatus.alccal.composables.common.SwipeToDeleteContainer

@Composable
fun HomeScreen(viewModel: HomeScreenViewModel) {
    val dateTimeList by viewModel.allDateTimes.collectAsState()
    val last24Hours by viewModel.last24HoursCount.collectAsState()
    val toastMessage by viewModel.toastMessage.collectAsState()

    val context = LocalContext.current
    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
            viewModel.clearToastMessage()
        }
    }

    val listState = rememberLazyListState()

    LaunchedEffect(dateTimeList) {
        listState.animateScrollToItem(0)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            Column(
                Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 0.dp),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Last 24 hours:",
                    textAlign = TextAlign.Start,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        last24Hours.toString(),
                        textAlign = TextAlign.Center,
                        fontWeight = MaterialTheme.typography.displayLarge.fontWeight,
                        fontSize = MaterialTheme.typography.displayLarge.fontSize
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_sports_bar_24),
                        contentDescription = "Drinks icon",
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

            }
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = listState
            ) {
                items(dateTimeList, key = { it.id }) { item ->
                    SwipeToDeleteContainer(
                        item = item,
                        onDelete = {
                            viewModel.deleteDateTime(item)
                        }
                    ) { dateTime ->
                        val date = dateTime.timestamp.substring(0, 10)
                        val time = dateTime.timestamp.substring(11, 19)
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 4.dp)
                        ) {
                            Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_sports_bar_24),
                                    contentDescription = "Drink icon",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(8.dp)
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = time,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(
                                        text = date,
                                        style = MaterialTheme.typography.labelMedium,
                                        modifier = Modifier
                                            .padding(end = 8.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

        }
        FloatingActionButton(
            onClick = { viewModel.insertDateTime() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .size(64.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_add_24),
                contentDescription = "Add a drink",
                modifier = Modifier.size(36.dp)
            )
        }
    }
}