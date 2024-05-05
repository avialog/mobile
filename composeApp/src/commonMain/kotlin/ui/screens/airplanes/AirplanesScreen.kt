package ui.screens.airplanes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import domain.model.Airplane
import ui.components.AirplaneCard
import ui.components.ErrorItem
import ui.components.LoaderFullScreen

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
                onAirplaneClick = {
                    onNewEvent(AirplanesEvent.AirplaneClick(airplane = airplane))
                },
                moreActions =
                    AirplaneCard.MoreActions(
                        onEditAirplaneClick = {
                            onNewEvent(AirplanesEvent.EditAirplaneClick(airplane = airplane))
                        },
                        onDeleteAirplaneClick = {
                            onNewEvent(AirplanesEvent.DeleteAirplaneClick(airplane = airplane))
                        },
                    ),
            )
        }
        item {
            Spacer(modifier = Modifier.height(height = 48.dp))
        }
    }
}
