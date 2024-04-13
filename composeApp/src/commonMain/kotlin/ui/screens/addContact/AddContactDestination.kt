package ui.screens.addContact

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun AddContactDestination(addContactComponent: AddContactComponent) {
    val state by addContactComponent.stateFlow.collectAsState()
    AddContactScreen(
        state = state,
        onNewEvent = addContactComponent::onNewEvent,
    )
}
