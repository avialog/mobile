package ui.screens.airplanes

import BaseMviViewModel
import ILogger
import Resource
import RetrySharedFlow
import com.arkivanov.decompose.ComponentContext
import domain.useCase.GetAirplanes
import kotlinx.coroutines.launch
import resourceFlowWithRetrying

class AirplanesComponent(
    componentContext: ComponentContext,
    private val onNavigateBack: () -> Unit,
    private val getAirplanes: GetAirplanes,
    private val logger: ILogger,
) : BaseMviViewModel<AirplanesState, AirplanesEvent>(
        componentContext = componentContext,
        initialState =
            AirplanesState(
                airplanesResource = Resource.Loading,
                isRequestInProgress = false,
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

    override fun onNewEvent(event: AirplanesEvent) {
        when (event) {
            AirplanesEvent.AddAirplaneClick -> {}
            is AirplanesEvent.AirplaneClick -> {}
            AirplanesEvent.BackClick -> onNavigateBack()
            is AirplanesEvent.DeleteAirplaneClick -> {}
            is AirplanesEvent.EditAirplaneClick -> {}
            AirplanesEvent.RetryClick -> retrySharedFlow.sendRetryEvent()
        }
    }
}
