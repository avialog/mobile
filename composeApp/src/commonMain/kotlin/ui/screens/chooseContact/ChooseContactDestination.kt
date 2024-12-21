package ui.screens.chooseContact

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun ChooseContactDestination(chooseContactComponent: ChooseContactComponent) {
    val state by chooseContactComponent.stateFlow.collectAsState()
    ChooseContactScreen(
        state = state,
        onNewEvent = chooseContactComponent::onNewEvent,
    )
}
