package ui.screens.login

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
fun LoginDestination(loginComponent: LoginComponent) {
    Column {
        val messagesChannelFlow =
            remember(loginComponent.errorNotificationFlow) {
                loginComponent.errorNotificationFlow.map {
                    InfoNotificationParams(
                        text = it,
                        duration = 3.seconds,
                    )
                }
            }
        InfoNotificationHandle(messagesFlow = messagesChannelFlow)
        val state by loginComponent.stateFlow.collectAsState()
        LoginScreen(
            state = state,
            onNewEvent = loginComponent::onNewEvent,
        )
    }
}
