package ui.screens.airplanes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import domain.model.Airplane
import ui.components.ErrorItem
import ui.components.LoaderFullScreen
import ui.components.MoreIconWithDropdown
import ui.components.Photo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AirplanesScreen(
    state: AirplanesState,
    onNewEvent: (AirplanesEvent) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Samoloty",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onNewEvent(AirplanesEvent.BackClick)
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
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onNewEvent(AirplanesEvent.AddAirplaneClick)
                },
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                )
            }
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .padding(paddingValues = innerPadding)
                    .consumeWindowInsets(innerPadding),
        ) {
            when (state.airplanesResource) {
                is Resource.Error -> {
                    ErrorItem(
                        onRetryClick = {
                            onNewEvent(AirplanesEvent.RetryClick)
                        },
                    )
                }
                Resource.Loading -> {
                    LoaderFullScreen()
                }
                is Resource.Success ->
                    Box {
                        Content(
                            airplanes = state.airplanesResource.data,
                            onNewEvent = onNewEvent,
                        )
                        if (state.isRequestInProgress) {
                            LoaderFullScreen(isOverlay = true)
                        }
                    }
            }
        }
    }
}

@Composable
private fun Content(
    airplanes: List<Airplane>,
    onNewEvent: (AirplanesEvent) -> Unit,
) {
    LazyColumn(
        contentPadding =
            PaddingValues(
                horizontal = 16.dp,
            ),
        verticalArrangement = Arrangement.spacedBy(space = 16.dp),
    ) {
        items(airplanes) { airplane ->
            AirplaneCard(
                airplane = airplane,
                onNewEvent = onNewEvent,
            )
        }
    }
}

@Composable
private fun AirplaneCard(
    airplane: Airplane,
    onNewEvent: (AirplanesEvent) -> Unit,
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
                )
                .height(height = 80.dp)
                .background(color = Color.White, shape = shape)
                .clip(shape = shape)
                .clickable { onNewEvent(AirplanesEvent.AddAirplaneClick) },
    ) {
        Photo(
            photoUrl = airplane.imageUrl,
            modifier =
                Modifier.fillMaxHeight()
                    .width(width = 100.dp),
        )
        ModelAndRegistration(
            airplane = airplane,
            modifier = Modifier.weight(weight = 1f),
        )
        MoreIconWithDropdown { onDismiss ->
            DropdownMenuItem(
                text = { Text("Edytuj samolot") },
                onClick = {
                    onNewEvent(AirplanesEvent.EditAirplaneClick(airplane = airplane))
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
                text = { Text("Usuń samolot") },
                onClick = {
                    onNewEvent(AirplanesEvent.DeleteAirplaneClick(airplane = airplane))
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

@Composable
private fun ModelAndRegistration(
    airplane: Airplane,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(space = 4.dp),
        modifier = modifier,
    ) {
        Text(
            text = airplane.airplaneModel,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = airplane.registrationNumber,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
