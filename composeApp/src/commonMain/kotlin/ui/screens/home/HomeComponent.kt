package ui.screens.home

import BaseMviViewModel
import com.arkivanov.decompose.ComponentContext
import data.repository.auth.IAuthRepository
import kotlinx.coroutines.launch

class HomeComponent(
    componentContext: ComponentContext,
    private val authRepository: IAuthRepository,
) : BaseMviViewModel<HomeState, HomeEvent>(
        componentContext = componentContext,
        initialState = HomeState(token = null),
    ) {
    override fun initialised() {
        viewModelScope.launch {
            authRepository.getAuthToken().also {
                updateState {
                    copy(token = it)
                }
            }
        }
    }

    override fun onNewEvent(event: HomeEvent) {
    }
}
