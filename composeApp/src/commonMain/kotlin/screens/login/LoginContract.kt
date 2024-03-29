package screens.login

data class LoginState(
    val email: String,
    val password: String,
    val isLoading: Boolean,
)

sealed interface LoginEvent {
    data object LoginClick : LoginEvent
}
