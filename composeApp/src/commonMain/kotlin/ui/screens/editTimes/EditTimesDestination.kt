package ui.screens.editTimes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier

@Composable
fun EditTimesDestination(editTimesComponent: EditTimesComponent) {
    Column(modifier = Modifier.fillMaxSize()) {
        EditTimesScreen(
            state = editTimesComponent.stateFlow.collectAsState().value,
            onNewEvent = editTimesComponent::onNewEvent,
        )
    }
}
