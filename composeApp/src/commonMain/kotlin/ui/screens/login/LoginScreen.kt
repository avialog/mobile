package ui.screens.login

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import ui.components.Loader
import ui.components.LoaderFullScreen

@Composable
fun LoginScreen(
    state: LoginState,
    onNewEvent: (LoginEvent) -> Unit,
) {
    if (state.showFullScreenLoader) {
        LoaderFullScreen()
    } else {
        Column {
            Text(text = "login")

            Button(
                onClick = {
                    onNewEvent(LoginEvent.LoginClick)
                },
            ) {
                Text("Login")
            }
            Button(
                onClick = {
                    onNewEvent(LoginEvent.RegisterClick)
                },
            ) {
                Text("Register")
            }
            if (state.isRequestInProgress) {
                Loader()
            }
        }
    }
}
