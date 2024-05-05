package ui.screens.chooseAirplane

import Resource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
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
fun ChooseAirplaneScreen(
    state: ChooseAirplaneState,
    onNewEvent: (ChooseAirplaneEvent) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Wybierz samolot",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onNewEvent(ChooseAirplaneEvent.BackClick)
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
            when (state.airplanesResource) {
                is Resource.Error -> {
                    ErrorItem(
                        onRetryClick = {
                            onNewEvent(ChooseAirplaneEvent.RetryClick)
                        },
                    )
                }
                Resource.Loading -> {
                    LoaderFullScreen()
                }
                is Resource.Success ->
                    Content(
                        airplanes = state.airplanesResource.data,
                        onNewEvent = onNewEvent,
                    )
            }
        }
    }
}

@Composable
private fun Content(
    airplanes: List<Airplane>,
    onNewEvent: (ChooseAirplaneEvent) -> Unit,
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
                    onNewEvent(ChooseAirplaneEvent.AirplaneClick(airplane = airplane))
                },
                moreActions = null,
            )
        }
        item {
            Spacer(modifier = Modifier.height(height = 48.dp))
        }
    }
}
