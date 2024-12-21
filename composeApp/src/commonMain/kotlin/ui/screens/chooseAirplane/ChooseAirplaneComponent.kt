package ui.screens.chooseAirplane

import BaseMviViewModel
import ILogger
import Resource
import RetrySharedFlow
import com.arkivanov.decompose.ComponentContext
import domain.model.Airplane
import domain.useCase.airplane.GetAirplanes
import kotlinx.coroutines.launch
import resourceFlowWithRetrying

class ChooseAirplaneComponent(
    componentContext: ComponentContext,
    private val getAirplanes: GetAirplanes,
    private val logger: ILogger,
    private val onAirplaneChosen: (Airplane) -> Unit,
    private val onNavigateBack: () -> Unit,
) : BaseMviViewModel<ChooseAirplaneState, ChooseAirplaneEvent>(
        componentContext = componentContext,
        initialState =
            ChooseAirplaneState(
                airplanesResource = Resource.Loading,
            ),
    ) {
    private val retrySharedFlow = RetrySharedFlow()

    override fun initialised() {
        viewModelScope.launch {
            resourceFlowWithRetrying(
                retrySharedFlow = retrySharedFlow,
                logger = logger,
            ) {
                getAirplanes()
            }.collect {
                updateState {
                    copy(airplanesResource = it)
                }
            }
        }
    }

    override fun onNewEvent(event: ChooseAirplaneEvent) {
        when (event) {
            is ChooseAirplaneEvent.AirplaneClick -> {
                onAirplaneChosen(event.airplane)
            }

            ChooseAirplaneEvent.BackClick -> onNavigateBack()
            ChooseAirplaneEvent.RetryClick -> retrySharedFlow.sendRetryEvent()
        }
    }
}
