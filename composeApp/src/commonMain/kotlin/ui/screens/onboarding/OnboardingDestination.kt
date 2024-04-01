package ui.screens.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun OnboardingDestination(onboardingComponent: OnboardingComponent) {
    val state by onboardingComponent.stateFlow.collectAsState()
    OnboardingScreen(
        state = state,
        onNewEvent = onboardingComponent::onNewEvent,
    )
}
