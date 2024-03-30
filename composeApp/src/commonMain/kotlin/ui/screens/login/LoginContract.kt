package ui.screens.login

data class LoginState(
    val email: String,
    val password: String,
    val isRequestInProgress: Boolean,
    val showFullScreenLoader: Boolean,
)

sealed interface LoginEvent {
    data object LoginClick : LoginEvent

    data object RegisterClick : LoginEvent

    data object ForgotPasswordClick : LoginEvent

    data class EmailChange(val email: String) : LoginEvent

    data class PasswordChange(val password: String) : LoginEvent
}
