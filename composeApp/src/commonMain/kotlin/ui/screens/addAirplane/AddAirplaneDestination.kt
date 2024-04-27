package ui.screens.addAirplane

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
fun AddAirplaneDestination(addAirplaneComponent: AddAirplaneComponent) {
    Column {
        val messagesChannelFlow =
            remember(addAirplaneComponent.errorNotificationFlow) {
                addAirplaneComponent.errorNotificationFlow.map {
                    InfoNotificationParams(
                        text = "Something went wrong!",
                        duration = 3.seconds,
                    )
                }
            }
        InfoNotificationHandle(messagesFlow = messagesChannelFlow)
        val state by addAirplaneComponent.stateFlow.collectAsState()
        AddAirplaneScreen(
            state = state,
            onNewEvent = addAirplaneComponent::onNewEvent,
        )
    }
}
