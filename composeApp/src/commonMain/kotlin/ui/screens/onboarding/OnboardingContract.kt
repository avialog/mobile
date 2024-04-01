package ui.screens.onboarding

data class OnboardingState(
    val showFullScreenLoader: Boolean,
)

sealed interface OnboardingEvent {
    data object SkipClick : OnboardingEvent
}
