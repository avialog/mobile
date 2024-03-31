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
    private val areLoginInputsValid: AreLoginInputsValid,
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
                    showErrorIfAny = false,
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
        makeLoginOrRegisterRequest { email, password ->
            loginWithEmailAndPassword(
                email = email,
                password = password,
            )
        }
    }

    private fun onRegisterClick() {
        makeLoginOrRegisterRequest { email, password ->
            registerWithEmailAndPassword(
                email = email,
                password = password,
            )
        }
    }

    private fun makeLoginOrRegisterRequest(request: suspend (String, String) -> Unit) {
        viewModelScope.launch {
            if (!actualState.isRequestInProgress) {
                val state = actualState
                if (areLoginInputsValid(
                        email = state.email,
                        password = state.password,
                    )
                ) {
                    updateState {
                        copy(
                            isRequestInProgress = true,
                            showErrorIfAny = false,
                        )
                    }
                    runCatching {
                        request(
                            state.email,
                            state.password,
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
                } else {
                    updateState {
                        copy(showErrorIfAny = true)
                    }
                }
            }
        }
    }
}