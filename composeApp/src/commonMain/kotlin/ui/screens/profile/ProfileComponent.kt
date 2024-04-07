package ui.screens.profile

import BaseMviViewModel
import ILogger
import Resource
import RetrySharedFlow
import com.arkivanov.decompose.ComponentContext
import domain.useCase.GetProfile
import domain.useCase.LogOut
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import resourceFlowWithRetrying

class ProfileComponent(
    componentContext: ComponentContext,
    private val onNavigateToLogin: () -> Unit,
    private val logger: ILogger,
    private val getProfile: GetProfile,
    private val logOut: LogOut,
) : BaseMviViewModel<ProfileState, ProfileEvent>(
        componentContext = componentContext,
        initialState =
            ProfileState(
                profileResource = Resource.Loading,
                isLogOutInProgress = false,
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
        when (event) {
            ProfileEvent.ContactsClick -> {}
            ProfileEvent.LogOutClick -> {
                if (!actualState.isLogOutInProgress) {
                    updateState {
                        copy(isLogOutInProgress = true)
                    }
                    viewModelScope.launch {
                        runCatching {
                            logOut()
                        }.onFailure {
                            ensureActive()
                            logger.w(it)
                            updateState {
                                copy(isLogOutInProgress = false)
                            }
                        }.onSuccess {
                            onNavigateToLogin()
                        }
                    }
                }
            }
            ProfileEvent.PasswordClick -> {}
            ProfileEvent.PlanesClick -> {}
            ProfileEvent.RetryClick -> retrySharedFlow.sendRetryEvent()
            ProfileEvent.UserDataClick -> {}
        }
    }
}
