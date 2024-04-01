package ui.screens.profile

data class ProfileState(
    val isLoading: Boolean,
)

sealed interface ProfileEvent
