package screens.login

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun LoginScreen(loginComponent: LoginComponent) {
    Column {
        Text(text = "login")

        Button(
            onClick = {
                loginComponent.loginClick()
            },
        ) {
            Text("Login")
        }
    }
}
