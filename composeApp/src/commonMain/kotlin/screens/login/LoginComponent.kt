package screens.login

import com.arkivanov.decompose.ComponentContext

class LoginComponent(
    componentContext: ComponentContext,
    private val onNavigateToHome: () -> Unit,
) : ComponentContext by componentContext {
    fun loginClick() {
        onNavigateToHome()
    }
}
