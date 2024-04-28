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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ui.utils.formatDayMonthYear

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
        modifier = modifier.padding(horizontal = 16.dp),
    ) {
        DatesRow(
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
        ChooseTimeCard(
            text = state.landingDate?.formatDayMonthYear() ?: "Wybierz datę",
            label = "Data startu*",
            onClick = {
            },
            modifier = Modifier.weight(weight = 1f),
        )
        ChooseTimeCard(
            text = state.landingDate?.formatDayMonthYear() ?: "Wybierz datę",
            label = "Data końcowa*",
            onClick = {
            },
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
