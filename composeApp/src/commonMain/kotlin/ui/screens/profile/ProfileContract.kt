package ui.screens.profile

import Resource
import domain.model.Profile

data class ProfileState(
    val profileResource: Resource<Profile>,
)

sealed interface ProfileEvent
