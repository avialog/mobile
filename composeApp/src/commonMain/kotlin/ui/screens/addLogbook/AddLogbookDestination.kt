package ui.screens.addLogbook

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import kotlinx.coroutines.flow.map
import ui.components.InfoNotificationHandle
import ui.components.InfoNotificationParams
import ui.screens.chooseAirplane.ChooseAirplaneDestination
import kotlin.time.Duration.Companion.seconds

@Composable
fun AddLogbookDestination(addLogbookComponent: AddLogbookComponent) {
    Column {
        val chooseAirplaneSlot by
            addLogbookComponent.chooseAirplaneSlot.subscribeAsState()
        chooseAirplaneSlot.child?.instance?.let {
            ChooseAirplaneDestination(chooseAirplaneComponent = it)
        }

        val messagesChannelFlow =
            remember(addLogbookComponent.errorNotificationFlow) {
                addLogbookComponent.errorNotificationFlow.map {
                    InfoNotificationParams(
                        text = "Something went wrong!",
                        duration = 3.seconds,
                    )
                }
            }
        InfoNotificationHandle(messagesFlow = messagesChannelFlow)
        val state by addLogbookComponent.stateFlow.collectAsState()
        AddLogbookScreen(
            state = state,
            onNewEvent = addLogbookComponent::onNewEvent,
        )
    }
}
