package ui.screens.profile

import BaseMviViewModel
import com.arkivanov.decompose.ComponentContext

class ProfileComponent(
    componentContext: ComponentContext,
) : BaseMviViewModel<ProfileState, ProfileEvent>(
        componentContext = componentContext,
        initialState = ProfileState(isLoading = true),
    ) {
    override fun onNewEvent(event: ProfileEvent) {
    }
}
