package ui.screens.contacts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun ContactsDestination(contactsComponent: ContactsComponent) {
    val state by contactsComponent.stateFlow.collectAsState()
    ContactsScreen(
        state = state,
        onNewEvent = contactsComponent::onNewEvent,
    )
}
