package ui.screens.profile

import Resource
import domain.model.Profile

data class ProfileState(
    val profileResource: Resource<Profile>,
    val isLogOutInProgress: Boolean,
)

sealed interface ProfileEvent {
    data object PlanesClick : ProfileEvent

    data object ContactsClick : ProfileEvent

    data object UserDataClick : ProfileEvent

    data object PasswordClick : ProfileEvent

    data object LogOutClick : ProfileEvent

    data object RetryClick : ProfileEvent
}
