package ui.screens.chooseAirplane

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier

@Composable
fun ChooseAirplaneDestination(chooseAirplaneComponent: ChooseAirplaneComponent) {
    Column(modifier = Modifier.fillMaxSize()) {
        ChooseAirplaneScreen(
            state = chooseAirplaneComponent.stateFlow.collectAsState().value,
            onNewEvent = chooseAirplaneComponent::onNewEvent,
        )
    }
}
