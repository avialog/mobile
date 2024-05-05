package ui.screens.addLogbook

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Clock
import compose.icons.fontawesomeicons.solid.PlaneArrival
import compose.icons.fontawesomeicons.solid.PlaneDeparture
import ui.components.AvialogDatePicker
import ui.components.TimePickerDialog
import ui.utils.formatDayMonthYear
import ui.utils.formatHourMinute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLogbookScreen(
    state: AddLogbookState,
    onNewEvent: (AddLogbookEvent) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Dodaj lot",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onNewEvent(AddLogbookEvent.BackClick)
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
                modifier = Modifier.weight(weight = 1f),
            )
            Button(
                onClick = {
                    onNewEvent(AddLogbookEvent.SaveClick)
                },
                shape = RoundedCornerShape(size = 4.dp),
                modifier =
                    Modifier.padding(all = 16.dp)
                        .heightIn(min = 54.dp).fillMaxWidth(),
            ) {
                Text(text = "Dodaj lot")
            }
        }
    }
}

@Composable
private fun Content(
    state: AddLogbookState,
    onNewEvent: (AddLogbookEvent) -> Unit,
    modifier: Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(space = 16.dp),
        modifier = modifier.padding(horizontal = 16.dp),
    ) {
        DatesRow(
            state = state,
            onNewEvent = onNewEvent,
        )
        TimesRow(
            state = state,
            onNewEvent = onNewEvent,
        )
        AirportInputsRow(
            state = state,
            onNewEvent = onNewEvent,
        )
    }
}

@Composable
private fun DatesRow(
    state: AddLogbookState,
    onNewEvent: (AddLogbookEvent) -> Unit,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(space = 16.dp)) {
        val showStartDateDialog = remember { mutableStateOf(false) }
        ChooseTimeCard(
            text = state.takeOffDate?.formatDayMonthYear() ?: "Wybierz datę",
            label = "Data startu (UTC)*",
            onClick = {
                showStartDateDialog.value = true
            },
            modifier = Modifier.weight(weight = 1f),
        )

        if (showStartDateDialog.value) {
            AvialogDatePicker(
                onDismiss = {
                    showStartDateDialog.value = false
                },
                onConfirm = {
                    onNewEvent(AddLogbookEvent.TakeOffDateChange(newDate = it))
                    showStartDateDialog.value = false
                },
                initial = state.takeOffDate,
            )
        }

        val showEndDateDialog = remember { mutableStateOf(false) }
        ChooseTimeCard(
            text = state.landingDate?.formatDayMonthYear() ?: "Wybierz datę",
            label = "Data lądowania (UTC)*",
            onClick = {
                showEndDateDialog.value = true
            },
            modifier = Modifier.weight(weight = 1f),
        )

        if (showEndDateDialog.value) {
            AvialogDatePicker(
                onDismiss = {
                    showEndDateDialog.value = false
                },
                onConfirm = {
                    onNewEvent(AddLogbookEvent.LandingDateChange(newDate = it))
                    showEndDateDialog.value = false
                },
                initial = state.landingDate,
            )
        }
    }
}

@Composable
private fun TimesRow(
    state: AddLogbookState,
    onNewEvent: (AddLogbookEvent) -> Unit,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(space = 16.dp)) {
        val showStartTimeDialog = remember { mutableStateOf(false) }
        ChooseTimeCard(
            text = state.takeOffTime?.formatHourMinute() ?: "Wybierz godzinę",
            label = "Czas startu (UTC)*",
            onClick = {
                showStartTimeDialog.value = true
            },
            icon = FontAwesomeIcons.Solid.Clock,
            modifier = Modifier.weight(weight = 1f),
        )

        if (showStartTimeDialog.value) {
            TimePickerDialog(
                onDismiss = {
                    showStartTimeDialog.value = false
                },
                onConfirm = {
                    onNewEvent(AddLogbookEvent.TakeOffTimeChange(newTime = it))
                    showStartTimeDialog.value = false
                },
                initial = state.takeOffTime,
            )
        }

        val showEndTimeDialog = remember { mutableStateOf(false) }
        ChooseTimeCard(
            text = state.landingTime?.formatHourMinute() ?: "Wybierz godzinę",
            label = "Czas lądowania (UTC)*",
            onClick = {
                showEndTimeDialog.value = true
            },
            icon = FontAwesomeIcons.Solid.Clock,
            modifier = Modifier.weight(weight = 1f),
        )

        if (showEndTimeDialog.value) {
            TimePickerDialog(
                onDismiss = {
                    showEndTimeDialog.value = false
                },
                onConfirm = {
                    onNewEvent(AddLogbookEvent.LandingTimeChange(newTime = it))
                    showEndTimeDialog.value = false
                },
                initial = state.landingTime,
            )
        }
    }
}

@Composable
private fun AirportInputsRow(
    state: AddLogbookState,
    onNewEvent: (AddLogbookEvent) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    Row(horizontalArrangement = Arrangement.spacedBy(space = 16.dp)) {
        OutlinedTextField(
            value = state.takeOffAirportCode,
            onValueChange = {
                onNewEvent(AddLogbookEvent.TakeOffAirportChange(it))
            },
            singleLine = true,
            label = {
                Text(
                    text = "Lotnisko startu*",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.labelSmall,
                )
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            leadingIcon = {
                Icon(
                    imageVector = FontAwesomeIcons.Solid.PlaneDeparture,
                    contentDescription = null,
                    modifier = Modifier.size(size = 16.dp),
                )
            },
            keyboardActions =
                KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Right)
                    },
                ),
            modifier = Modifier.weight(weight = 1f),
        )
        OutlinedTextField(
            value = state.landingAirportCode,
            onValueChange = {
                onNewEvent(AddLogbookEvent.LandingAirportChange(it))
            },
            singleLine = true,
            label = {
                Text(
                    text = "Lotnisko lądowania*",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.labelSmall,
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = FontAwesomeIcons.Solid.PlaneArrival,
                    contentDescription = null,
                    modifier = Modifier.size(size = 16.dp),
                )
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            modifier = Modifier.weight(weight = 1f),
        )
    }
}

@Composable
private fun ChooseTimeCard(
    text: String,
    label: String,
    icon: ImageVector = Icons.Filled.DateRange,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(space = 2.dp),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        OutlinedCard(
            shape = RoundedCornerShape(size = 4.dp),
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier =
                    Modifier.padding(
                        horizontal = 6.dp,
                        vertical = 12.dp,
                    ),
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(size = 24.dp),
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}
