package screens.login

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun LoginScreen(
    state: LoginState,
    onNewEvent: (LoginEvent) -> Unit,
) {
    Column {
        Text(text = "login")

        Button(
            onClick = {
                onNewEvent(LoginEvent.LoginClick)
            },
        ) {
            Text("Login")
        }
    }
}
