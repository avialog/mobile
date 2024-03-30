package ui.screens.login

import BaseMviViewModel
import com.arkivanov.decompose.ComponentContext
import domain.IsUserLoggedIn
import domain.LoginWithEmailAndPassword
import domain.RegisterWithEmailAndPassword
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch

class LoginComponent(
    componentContext: ComponentContext,
    private val loginWithEmailAndPassword: LoginWithEmailAndPassword,
    private val registerWithEmailAndPassword: RegisterWithEmailAndPassword,
    private val isUserLoggedIn: IsUserLoggedIn,
    private val onNavigateToHome: () -> Unit,
) :
    BaseMviViewModel<LoginState, LoginEvent>(
            componentContext = componentContext,
            initialState =
                LoginState(
                    email = "",
                    password = "",
                    isRequestInProgress = false,
                    showFullScreenLoader = true,
                ),
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

    override fun onNewEvent(event: LoginEvent) {
        when (event) {
            LoginEvent.LoginClick -> {
                onLoginClick()
            }

            LoginEvent.RegisterClick -> {
                onRegisterClick()
            }

            is LoginEvent.EmailChange -> {
                updateState {
                    copy(email = event.email)
                }
            }
            is LoginEvent.PasswordChange -> {
                updateState {
                    copy(password = event.password)
                }
            }

            LoginEvent.ForgotPasswordClick -> {}
        }
    }

    private fun onLoginClick() {
        viewModelScope.launch {
            if (!actualState.isRequestInProgress) {
                updateState {
                    copy(isRequestInProgress = true)
                }
                runCatching {
                    loginWithEmailAndPassword(
                        email = actualState.email,
                        password = actualState.password,
                    )
                }.onSuccess {
                    onNavigateToHome()
                }.onFailure {
                    ensureActive()
                    it.printStackTrace()
                    updateState {
                        copy(isRequestInProgress = false)
                    }
                }
            }
        }
    }

    private fun onRegisterClick() {
        viewModelScope.launch {
            if (!actualState.isRequestInProgress) {
                runCatching {
                    registerWithEmailAndPassword(
                        email = actualState.email,
                        password = actualState.password,
                    )
                }.onSuccess {
                    onNavigateToHome()
                }.onFailure {
                    ensureActive()
                    it.printStackTrace()
                }
            }
        }
    }
}
