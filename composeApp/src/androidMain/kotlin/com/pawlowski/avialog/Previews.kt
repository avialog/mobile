package com.pawlowski.avialog

import Resource
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import domain.model.Profile
import ui.screens.login.LoginScreen
import ui.screens.login.LoginState
import ui.screens.profile.ProfileScreen
import ui.screens.profile.ProfileState

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

@Preview(
    showBackground = true,
)
@Composable
private fun ProfileScreenPreview() {
    ProfileScreen(
        state =
        ProfileState(
            profileResource = Resource.Success(
                data = Profile(
                    firstName = "Wojciech",
                    lastName = "Kowalski",
                    avatarUrl = null,
                    email = "kowalski@gmail.com",
                ),
            ),
            isLogOutInProgress = false,
        ),
        onNewEvent = {
        },
    )
}