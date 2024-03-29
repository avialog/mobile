package screens.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun LoginDestination(loginComponent: LoginComponent) {
    val state by loginComponent.stateFlow.collectAsState()
    LoginScreen(
        state = state,
        onNewEvent = loginComponent::onNewEvent,
    )
}
