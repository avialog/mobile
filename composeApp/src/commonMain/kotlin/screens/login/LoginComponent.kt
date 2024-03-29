package screens.login

import BaseMviViewModel
import com.arkivanov.decompose.ComponentContext
import domain.LoginWithEmailAndPassword
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch

class LoginComponent(
    componentContext: ComponentContext,
    private val loginWithEmailAndPassword: LoginWithEmailAndPassword,
    private val onNavigateToHome: () -> Unit,
) :
    BaseMviViewModel<LoginState, LoginEvent>(
            componentContext = componentContext,
            initialState =
                LoginState(
                    email = "",
                    password = "",
                    isLoading = false,
                ),
        ) {
    override fun onNewEvent(event: LoginEvent) {
        when (event) {
            LoginEvent.LoginClick -> {
                onLoginClick()
            }
        }
    }

    private fun onLoginClick() {
        viewModelScope.launch {
            if (!actualState.isLoading) {
                runCatching {
                    loginWithEmailAndPassword(
                        email = actualState.email,
                        password = actualState.password,
                    )
                }.onSuccess {
                    ensureActive()
                    onNavigateToHome()
                }
            }
        }
    }
}
