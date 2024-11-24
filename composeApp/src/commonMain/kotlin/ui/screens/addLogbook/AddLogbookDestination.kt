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
import ui.screens.chooseAirplane.ChooseAirplaneComponent
import ui.screens.chooseAirplane.ChooseAirplaneDestination
import ui.screens.chooseContact.ChooseContactComponent
import ui.screens.chooseContact.ChooseContactDestination
import kotlin.time.Duration.Companion.seconds

@Composable
fun AddLogbookDestination(addLogbookComponent: AddLogbookComponent) {
    Column {
        val chooseAirplaneSlot by
            addLogbookComponent.addLogbookChildSlot.subscribeAsState()
        chooseAirplaneSlot.child?.instance?.let { component ->
            when (component) {
                is ChooseAirplaneComponent ->
                    ChooseAirplaneDestination(
                        chooseAirplaneComponent = component,
                    )
                is ChooseContactComponent ->
                    ChooseContactDestination(
                        chooseContactComponent = component,
                    )
            }
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
