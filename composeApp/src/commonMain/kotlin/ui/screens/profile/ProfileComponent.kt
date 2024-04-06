package ui.screens.profile

import BaseMviViewModel
import com.arkivanov.decompose.ComponentContext
import domain.useCase.GetProfile
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch

class ProfileComponent(
    componentContext: ComponentContext,
    private val getProfile: GetProfile,
) : BaseMviViewModel<ProfileState, ProfileEvent>(
        componentContext = componentContext,
        initialState = ProfileState(isLoading = true),
    ) {
    override fun initialised() {
        viewModelScope.launch {
            runCatching {
                getProfile()
            }.onFailure {
                ensureActive()
                it.printStackTrace()
            }.onSuccess {
                println("Success $it")
            }
        }
    }

    override fun onNewEvent(event: ProfileEvent) {
    }
}
