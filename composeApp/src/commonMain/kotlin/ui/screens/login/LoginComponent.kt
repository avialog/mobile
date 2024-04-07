package ui.screens.login

import BaseMviViewModel
import ILogger
import com.arkivanov.decompose.ComponentContext
import domain.useCase.LoginWithEmailAndPassword
import domain.useCase.RegisterWithEmailAndPassword
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class LoginComponent(
    componentContext: ComponentContext,
    private val loginWithEmailAndPassword: LoginWithEmailAndPassword,
    private val registerWithEmailAndPassword: RegisterWithEmailAndPassword,
    private val areLoginInputsValid: AreLoginInputsValid,
    private val logger: ILogger,
    private val onNavigateToHome: () -> Unit,
) :
    BaseMviViewModel<LoginState, LoginEvent>(
            componentContext = componentContext,
            initialState =
                LoginState(
                    email = "",
                    password = "",
                    isRequestInProgress = false,
                    showErrorIfAny = false,
                ),
        ) {
    private val errorNotificationChannel = Channel<String>(Channel.UNLIMITED)
    val errorNotificationFlow = errorNotificationChannel.receiveAsFlow()

    override fun initialised() {
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
                        logger.w(it)
                        errorNotificationChannel.send("Something went wrong!")
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
