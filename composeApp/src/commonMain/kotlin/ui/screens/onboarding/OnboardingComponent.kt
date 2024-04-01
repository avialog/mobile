package ui.screens.onboarding

import BaseMviViewModel
import com.arkivanov.decompose.ComponentContext
import domain.IsUserLoggedIn
import kotlinx.coroutines.launch

class OnboardingComponent(
    componentContext: ComponentContext,
    private val onNavigateToHome: () -> Unit,
    private val onNavigateToLogin: () -> Unit,
    private val isUserLoggedIn: IsUserLoggedIn,
) : BaseMviViewModel<OnboardingState, OnboardingEvent>(
        componentContext = componentContext,
        initialState = OnboardingState(showFullScreenLoader = true),
    ) {
    override fun initialised() {
        viewModelScope.launch {
            if (isUserLoggedIn()) {
                onNavigateToHome()
            } else {
                updateState {
                    copy(showFullScreenLoader = false)
                }
            }
        }
    }

    override fun onNewEvent(event: OnboardingEvent) {
        when (event) {
            is OnboardingEvent.SkipClick -> onNavigateToLogin()
        }
    }
}
