package ui.screens.flights

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import domain.model.Logbook
import ui.components.ErrorItem
import ui.components.LoaderFullScreen
import ui.components.MoreIconWithDropdown
import ui.components.RoleBox
import ui.utils.formatDayMonthYear
import ui.utils.formatHourMinute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightsScreen(
    state: FlightsState,
    onNewEvent: (FlightsEvent) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onNewEvent(FlightsEvent.AddFlightClick)
                },
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                )
            }
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Loty",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { padding ->
        Box(
            modifier =
                Modifier
                    .padding(paddingValues = padding)
                    .consumeWindowInsets(paddingValues = padding),
        ) {
            when (state.flightsResource) {
                is Resource.Error -> ErrorItem(onRetryClick = { onNewEvent(FlightsEvent.RetryClick) })
                Resource.Loading -> LoaderFullScreen()
                is Resource.Success -> {
                    Content(
                        flights = state.flightsResource.data,
                        onNewEvent = onNewEvent,
                    )
                    if (state.requestInProgress) {
                        LoaderFullScreen(isOverlay = true)
                    }
                }
            }
        }
    }
}

@Composable
private fun Content(
    flights: List<Logbook>,
    onNewEvent: (FlightsEvent) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(space = 16.dp),
        modifier = Modifier.verticalScroll(state = rememberScrollState()),
    ) {
        flights.forEach {
            LogbookItem(
                logbook = it,
                onNewEvent = onNewEvent,
            )
        }
    }
}

@Composable
fun LogbookItem(
    logbook: Logbook,
    onNewEvent: (FlightsEvent) -> Unit,
) {
    val shape = RoundedCornerShape(size = 8.dp)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(space = 16.dp),
        modifier =
            Modifier
                .shadow(
                    elevation = 4.dp,
                    spotColor = Color(0x40000000),
                    ambientColor = Color(0x40000000),
                    shape = shape,
                ).height(height = 80.dp)
                .background(color = Color.White, shape = shape)
                .clip(shape = shape)
                .clickable { },
    ) {
        Column(modifier = Modifier.padding(all = 16.dp)) {
            Text(
                "${logbook.takeOffDate.formatDayMonthYear()} ${logbook.takeOffTime.formatHourMinute()}, total: ${logbook.totalBlockTime?.hours ?: 0}h ${logbook.totalBlockTime?.minutes ?: 0}min",
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(space = 16.dp),
            ) {
                Text(
                    "${logbook.takeOffAirportCode} - ${logbook.landingAirportCode}",
                    modifier = Modifier.weight(weight = 1f),
                )
                RoleBox(
                    role = logbook.myRole,
                    modifier = Modifier.weight(weight = 1f),
                )

                MoreIconWithDropdown { onDismiss ->
                    DropdownMenuItem(
                        text = { Text("Edytuj lot") },
                        onClick = {
                            onNewEvent(FlightsEvent.EditFlightClick(logbook = logbook))
                            onDismiss()
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Outlined.Edit,
                                contentDescription = null,
                            )
                        },
                    )
                    DropdownMenuItem(
                        text = { Text("Usuń lot") },
                        onClick = {
                            onNewEvent(FlightsEvent.RemoveFlightClick(logbook = logbook))
                            onDismiss()
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Outlined.Delete,
                                contentDescription = null,
                            )
                        },
                    )
                }
            }
        }
    }
}
