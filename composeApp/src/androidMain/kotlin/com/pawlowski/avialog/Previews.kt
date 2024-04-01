package com.pawlowski.avialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import ui.screens.login.LoginScreen
import ui.screens.login.LoginState

@Preview(
    showBackground = true,
)
@Composable
private fun LoginScreenPreview() {
    LoginScreen(
        state =
            LoginState(
                email = "",
                password = "",
                isRequestInProgress = true,
                showErrorIfAny = true,
            ),
        onNewEvent = {
        },
    )
}
