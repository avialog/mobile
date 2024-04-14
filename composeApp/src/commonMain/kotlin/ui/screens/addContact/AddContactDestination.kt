package ui.screens.addContact

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.map
import ui.components.InfoNotificationHandle
import ui.components.InfoNotificationParams
import kotlin.time.Duration.Companion.seconds

@Composable
fun AddContactDestination(addContactComponent: AddContactComponent) {
    Column {
        val messagesChannelFlow =
            remember(addContactComponent.errorNotificationFlow) {
                addContactComponent.errorNotificationFlow.map {
                    InfoNotificationParams(
                        text = "Something went wrong!",
                        duration = 3.seconds,
                    )
                }
            }
        InfoNotificationHandle(messagesFlow = messagesChannelFlow)
        val state by addContactComponent.stateFlow.collectAsState()
        AddContactScreen(
            state = state,
            onNewEvent = addContactComponent::onNewEvent,
        )
    }
}
