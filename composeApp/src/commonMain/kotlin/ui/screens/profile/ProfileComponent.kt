package ui.screens.profile

import BaseMviViewModel
import ILogger
import Resource
import RetrySharedFlow
import com.arkivanov.decompose.ComponentContext
import domain.useCase.GetProfile
import kotlinx.coroutines.launch
import resourceFlowWithRetrying

class ProfileComponent(
    componentContext: ComponentContext,
    private val logger: ILogger,
    private val getProfile: GetProfile,
) : BaseMviViewModel<ProfileState, ProfileEvent>(
        componentContext = componentContext,
        initialState =
            ProfileState(
                profileResource = Resource.Loading,
            ),
    ) {
    private val retrySharedFlow = RetrySharedFlow()

    override fun initialised() {
        viewModelScope.launch {
            resourceFlowWithRetrying(
                logger = logger,
                retrySharedFlow = retrySharedFlow,
            ) {
                getProfile()
            }.collect {
                updateState {
                    copy(profileResource = it)
                }
            }
        }
    }

    override fun onNewEvent(event: ProfileEvent) {
    }
}
