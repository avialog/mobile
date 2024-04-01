package ui.screens.flights

import BaseMviViewModel
import com.arkivanov.decompose.ComponentContext
import data.repository.auth.IAuthRepository
import kotlinx.coroutines.launch

class FlightsComponent(
    componentContext: ComponentContext,
    private val authRepository: IAuthRepository,
) : BaseMviViewModel<FlightsState, FlightsEvent>(
        componentContext = componentContext,
        initialState = FlightsState(token = null),
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

    override fun onNewEvent(event: FlightsEvent) {
    }
}
