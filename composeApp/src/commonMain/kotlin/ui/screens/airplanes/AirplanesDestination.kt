package ui.screens.airplanes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun AirplanesDestination(airplanesComponent: AirplanesComponent) {
    val state by airplanesComponent.stateFlow.collectAsState()
    AirplanesScreen(
        state = state,
        onNewEvent = airplanesComponent::onNewEvent,
    )
}
