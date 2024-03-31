package ui.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun HomeDestination(homeComponent: HomeComponent) {
    val state by homeComponent.stateFlow.collectAsState()
    HomeScreen(
        state = state,
        onNewEvent = homeComponent::onNewEvent,
    )
}
