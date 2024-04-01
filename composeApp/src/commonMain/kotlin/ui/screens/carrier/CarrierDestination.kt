package ui.screens.carrier

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun CarrierDestination(carrierComponent: CarrierComponent) {
    val state by carrierComponent.stateFlow.collectAsState()
    CarrierScreen(
        state = state,
        onNewEvent = carrierComponent::onNewEvent,
    )
}
