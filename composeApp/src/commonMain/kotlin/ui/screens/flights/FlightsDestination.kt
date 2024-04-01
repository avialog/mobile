package ui.screens.flights

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun FlightsDestination(flightsComponent: FlightsComponent) {
    val state by flightsComponent.stateFlow.collectAsState()
    FlightsScreen(
        state = state,
        onNewEvent = flightsComponent::onNewEvent,
    )
}
