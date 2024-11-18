package ui.screens.addLogbook

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.ArrowDown
import compose.icons.fontawesomeicons.solid.Clock
import compose.icons.fontawesomeicons.solid.MapMarker
import compose.icons.fontawesomeicons.solid.Minus
import compose.icons.fontawesomeicons.solid.MinusCircle
import compose.icons.fontawesomeicons.solid.PlaneArrival
import compose.icons.fontawesomeicons.solid.PlaneDeparture
import compose.icons.fontawesomeicons.solid.Plus
import domain.model.Landing
import domain.model.Passenger
import domain.model.Role
import domain.model.toBoxColor
import ui.components.AirplaneCard
import ui.components.AvialogDatePicker
import ui.components.TimePickerDialog
import ui.screens.chooseAirportCodeDialog.ChooseAirportCodeDialog
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
                    Modifier
                        .padding(all = 16.dp)
                        .heightIn(min = 54.dp)
                        .fillMaxWidth(),
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
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(space = 16.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = modifier,
    ) {
        item {
            DatesRow(
                state = state,
                onNewEvent = onNewEvent,
            )
        }
        item {
            TimesRow(
                state = state,
                onNewEvent = onNewEvent,
            )
        }
        item {
            AirportInputsRow(
                state = state,
                onNewEvent = onNewEvent,
            )
        }
        item {
            AirplaneCard(
                airplane = state.airplane,
                onAirplaneClick = {
                    onNewEvent(AddLogbookEvent.ChooseAirplaneClick)
                },
                moreActions = null,
                textIfAirplaneNull = "Wybierz samolot",
            )
        }

        Landings(
            state = state,
            onNewEvent = onNewEvent,
        )

        item {
            Passengers(
                state = state,
                onNewEvent = onNewEvent,
            )
        }
    }
}

@Composable
private fun Passengers(
    state: AddLogbookState,
    onNewEvent: (AddLogbookEvent) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(space = 4.dp),
    ) {
        Text(
            text = "Załoga",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.secondary,
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(space = 10.dp),
            modifier =
                Modifier
                    .shadow(
                        elevation = 4.dp,
                        spotColor = Color(0x40000000),
                        ambientColor = Color(0x40000000),
                    ).background(
                        color = Color(0xFFFFFFFF),
                        shape = RoundedCornerShape(size = 8.dp),
                    ).padding(horizontal = 16.dp),
        ) {
            PassengerRow(
                passenger =
                    Passenger(
                        company = null,
                        emailAddress = "",
                        firstName = "Ty",
                        lastName = null,
                        note = null,
                        phone = null,
                        role = state.myRole,
                    ),
            )
            HorizontalDivider()
            state.passengers.forEachIndexed { index, passenger ->
                PassengerRow(
                    passenger = passenger,
                    onDeleteClick = {
                        onNewEvent(AddLogbookEvent.RemovePassengerClick(index = index))
                    },
                )
                HorizontalDivider()
            }

            TextButton(
                onClick = {
                    onNewEvent(AddLogbookEvent.AddPassengerClick)
                },
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
            ) {
                Text(text = "+ Dodaj załogę")
            }
        }
    }
}

@Composable
private fun PassengerRow(
    passenger: Passenger,
    modifier: Modifier = Modifier,
    onDeleteClick: (() -> Unit)? = null,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(space = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        PassengerRoleBox(role = passenger.role)
        Column(
            verticalArrangement = Arrangement.spacedBy(space = 4.dp),
            modifier = Modifier.weight(weight = 1f),
        ) {
            Text(
                text = passenger.firstName,
                style = MaterialTheme.typography.labelLarge,
            )
            Text(
                text = "Captain",
                style = MaterialTheme.typography.bodySmall,
            )
        }
        if (onDeleteClick != null) {
            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = null,
                )
            }
        }
    }
}

@Composable
private fun PassengerRoleBox(role: Role) {
    Box(
        modifier =
            Modifier
                .size(size = 31.dp)
                .background(
                    color = role.toBoxColor(),
                    shape = RoundedCornerShape(size = 4.dp),
                ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = role.name,
            color = Color.White,
            style = MaterialTheme.typography.titleSmall,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
private fun LazyListScope.Landings(
    state: AddLogbookState,
    onNewEvent: (AddLogbookEvent) -> Unit,
) {
    item {
        Text(
            text = "Lądowania",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.secondary,
        )
    }
    itemsIndexed(
        state.landings,
        key = { index, landing ->
            landing.id
        },
    ) { index, landing ->
        Column(modifier = Modifier.animateItemPlacement()) {
            LandingCard(
                landing = landing,
                onLandingChange = {
                    onNewEvent(AddLogbookEvent.LandingChange(index, it))
                },
                showRemoveLandingButton = index != state.landings.lastIndex,
                onRemoveLanding = {
                    onNewEvent(AddLogbookEvent.RemoveLandingClick(index))
                },
            )
            if (index != state.landings.lastIndex) {
                Icon(
                    imageVector = FontAwesomeIcons.Solid.ArrowDown,
                    contentDescription = null,
                    modifier =
                        Modifier
                            .fillParentMaxWidth()
                            .wrapContentWidth(align = Alignment.CenterHorizontally)
                            .padding(vertical = 4.dp)
                            .size(size = 16.dp),
                )
            }
        }
    }
    item {
        TextButton(
            onClick = {
                onNewEvent(AddLogbookEvent.AddLandingClick)
            },
            modifier =
                Modifier
                    .fillParentMaxWidth()
                    .wrapContentWidth(align = Alignment.CenterHorizontally),
        ) {
            Text(text = "+ Dodaj lądowanie")
        }
    }
}

@Composable
private fun LandingCard(
    landing: Landing,
    onLandingChange: (Landing) -> Unit,
    showRemoveLandingButton: Boolean,
    onRemoveLanding: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(space = 4.dp),
        modifier =
            Modifier
                .shadow(
                    elevation = 4.dp,
                    spotColor = Color(0x40000000),
                    ambientColor = Color(0x40000000),
                ).background(
                    color = Color(0xFFFFFFFF),
                    shape = RoundedCornerShape(size = 8.dp),
                ),
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            val showDialog =
                remember {
                    mutableStateOf(false)
                }
            ChooseAirportCodeDialog(
                show = showDialog.value,
                onDismiss = {
                    showDialog.value = false
                },
                onAirportCodeSelected = {
                    onLandingChange(landing.copy(airportCode = it))
                    showDialog.value = false
                },
                initialAirportCode = landing.airportCode,
            )
            Row(
                modifier =
                    Modifier
                        .align(alignment = Alignment.TopCenter)
                        .padding(top = 8.dp)
                        .clip(shape = RoundedCornerShape(size = 16.dp))
                        .widthIn(min = 150.dp)
                        .clickable {
                            showDialog.value = true
                        }.padding(
                            vertical = 4.dp,
                            horizontal = 24.dp,
                        ),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = FontAwesomeIcons.Solid.MapMarker,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 4.dp).size(size = 16.dp),
                    tint = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = landing.airportCode.ifEmpty { "Kliknij aby wpisać..." },
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            if (showRemoveLandingButton) {
                IconButton(
                    onClick = onRemoveLanding,
                    modifier =
                        Modifier
                            .align(alignment = Alignment.TopEnd),
                ) {
                    Icon(
                        imageVector = FontAwesomeIcons.Solid.MinusCircle,
                        contentDescription = null,
                        modifier =
                            Modifier
                                .size(size = 16.dp),
                        tint = Color.Red,
                    )
                }
            }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier =
                Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp),
        ) {
            Icon(
                imageVector = FontAwesomeIcons.Solid.PlaneDeparture,
                contentDescription = null,
                modifier = Modifier.size(size = 16.dp),
            )
            Text(
                text = "Visual",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(weight = 1f),
            )
            NumberOfLandingsPicker(
                label = "Noc",
                number = landing.nightCount,
                onNumberChange = {
                    onLandingChange(landing.copy(nightCount = it))
                },
            )
            NumberOfLandingsPicker(
                label = "Dzień",
                number = landing.dayCount,
                onNumberChange = {
                    onLandingChange(landing.copy(dayCount = it))
                },
            )
        }
    }
}

@Composable
private fun NumberOfLandingsPicker(
    label: String,
    number: Long,
    onNumberChange: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        IconButton(
            onClick = {
                onNumberChange((number - 1L).coerceAtLeast(minimumValue = 0L))
            },
            modifier =
                Modifier.size(size = 24.dp),
        ) {
            Icon(
                imageVector = FontAwesomeIcons.Solid.Minus,
                contentDescription = null,
                modifier = Modifier.size(size = 12.dp),
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(space = 2.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.secondary,
            )
            Text(
                text = number.toString(),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
            )
        }
        IconButton(
            onClick = {
                onNumberChange(number + 1L)
            },
            modifier =
                Modifier.size(size = 24.dp),
        ) {
            Icon(
                imageVector = FontAwesomeIcons.Solid.Plus,
                contentDescription = null,
                modifier = Modifier.size(size = 12.dp),
            )
        }
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
            keyboardOptions =
                KeyboardOptions(
                    imeAction = ImeAction.Next,
                    capitalization = KeyboardCapitalization.Characters,
                ),
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
            keyboardOptions =
                KeyboardOptions(
                    imeAction = ImeAction.Done,
                    capitalization = KeyboardCapitalization.Characters,
                ),
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
