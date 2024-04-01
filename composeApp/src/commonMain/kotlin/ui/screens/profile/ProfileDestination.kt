package ui.screens.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun ProfileDestination(profileComponent: ProfileComponent) {
    val state by profileComponent.stateFlow.collectAsState()
    ProfileScreen(
        state = state,
        onNewEvent = profileComponent::onNewEvent,
    )
}
