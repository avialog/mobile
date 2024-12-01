package ui.screens.editTimes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.datetime.DateTimePeriod

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTimesScreen(
    state: EditTimesState,
    onNewEvent: (EditTimesEvent) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Edytuj czasy",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onNewEvent(EditTimesEvent.SaveClick)
                        },
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .padding(paddingValues = innerPadding)
                    .consumeWindowInsets(innerPadding),
        ) {
            Content(
                state = state,
                onNewEvent = onNewEvent,
            )
        }
    }
}

@Composable
private fun Content(
    state: EditTimesState,
    onNewEvent: (EditTimesEvent) -> Unit,
) {
    LazyColumn(
        contentPadding =
            PaddingValues(
                horizontal = 16.dp,
            ),
        verticalArrangement = Arrangement.spacedBy(space = 16.dp),
    ) {
        listOf(
            Triple(
                "Total block",
                state.totalBlockTime,
                { newTime: DateTimePeriod? ->
                    onNewEvent(
                        EditTimesEvent.TotalBlockChange(
                            newTime ?: DateTimePeriod(),
                        ),
                    )
                },
            ),
            Triple(
                "Cross country",
                state.crossCountryTime,
                { newTime: DateTimePeriod? ->
                    onNewEvent(EditTimesEvent.CrossCountryChange(newTime))
                },
            ),
            Triple(
                "Dual Received",
                state.dualReceivedTime,
                { newTime: DateTimePeriod? ->
                    onNewEvent(EditTimesEvent.DualReceivedChange(newTime))
                },
            ),
            Triple(
                "Dual Given",
                state.dualGivenTime,
                { newTime: DateTimePeriod? ->
                    onNewEvent(EditTimesEvent.DualGivenChange(newTime))
                },
            ),
            Triple(
                "IFR Actual",
                state.ifrActualTime,
                { newTime: DateTimePeriod? ->
                    onNewEvent(EditTimesEvent.IfrActualChange(newTime))
                },
            ),
            Triple(
                "IFR Sumulated",
                state.ifrSimulatedTime,
                { newTime: DateTimePeriod? ->
                    onNewEvent(EditTimesEvent.IfrSimulatedChange(newTime))
                },
            ),
            Triple(
                "IFR",
                state.ifrTime,
                { newTime: DateTimePeriod? ->
                    onNewEvent(EditTimesEvent.IfrTimeChange(newTime))
                },
            ),
            Triple(
                "Night",
                state.nightTime,
                { newTime: DateTimePeriod? ->
                    onNewEvent(EditTimesEvent.NightChange(newTime))
                },
            ),
            Triple(
                "Pilot in command",
                state.pilotInCommandTime,
                { newTime: DateTimePeriod? ->
                    onNewEvent(EditTimesEvent.PilotInCommandChange(newTime))
                },
            ),
            Triple(
                "Second in command",
                state.secondInCommandTime,
                { newTime: DateTimePeriod? ->
                    onNewEvent(EditTimesEvent.SecondInCommandChange(newTime))
                },
            ),
            Triple(
                "Simulator",
                state.simulatorTime,
                { newTime: DateTimePeriod? ->
                    onNewEvent(EditTimesEvent.SimulatorChange(newTime))
                },
            ),
        ).forEach { (title, time, onTimeChange) ->
            item {
                TimeRow(
                    title = title,
                    time = time,
                    onTimeChange = onTimeChange,
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(height = 48.dp))
        }
    }
}

@Composable
private fun TimeRow(
    time: DateTimePeriod?,
    title: String,
    onTimeChange: (DateTimePeriod?) -> Unit,
) {
    val hours =
        remember {
            mutableStateOf(time?.hours?.toString() ?: "")
        }
    val minutes =
        remember {
            mutableStateOf(time?.minutes?.toString() ?: "")
        }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
    ) {
        Text(
            text = title,
            modifier = Modifier.weight(weight = 2f),
        )
        OutlinedTextField(
            value = hours.value,
            onValueChange = { newValue ->
                if (newValue.isNotEmpty() && newValue.toIntOrNull() == null) {
                    return@OutlinedTextField
                }
                hours.value = newValue
                val newValueInt = newValue.toIntOrNull()
                onTimeChange(
                    DateTimePeriod(hours = newValueInt ?: 0, minutes = time?.minutes ?: 0).takeUnless {
                        newValueInt.isZeroOrNull() && time?.minutes.isZeroOrNull()
                    },
                )
            },
            label = {
                Text(text = "hours")
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(weight = 1f),
        )
        OutlinedTextField(
            value = minutes.value,
            onValueChange = { newValue ->
                if (newValue.isNotEmpty() && newValue.toIntOrNull() == null) {
                    return@OutlinedTextField
                }
                minutes.value = newValue
                val newValueInt = newValue.toIntOrNull()
                onTimeChange(
                    DateTimePeriod(hours = time?.hours ?: 0, minutes = newValueInt ?: 0).takeUnless {
                        newValueInt.isZeroOrNull() && time?.hours.isZeroOrNull()
                    },
                )
            },
            label = {
                Text(text = "min")
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(weight = 1f),
        )
    }
}

private fun Int?.isZeroOrNull() = this == 0 || this == null
