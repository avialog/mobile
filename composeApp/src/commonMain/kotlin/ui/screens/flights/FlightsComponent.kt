package ui.screens.flights

import BaseMviViewModel
import ILogger
import Resource
import RetrySharedFlow
import com.arkivanov.decompose.ComponentContext
import domain.useCase.GetFlights
import kotlinx.coroutines.launch
import resourceFlowWithRetrying

class FlightsComponent(
    componentContext: ComponentContext,
    private val onNavigateToAddLogbook: () -> Unit,
    private val getFlights: GetFlights,
    private val logger: ILogger,
) : BaseMviViewModel<FlightsState, FlightsEvent>(
        componentContext = componentContext,
        initialState =
            FlightsState(
                flightsResource = Resource.Loading,
            ),
    ) {
    private val retrySharedFlow = RetrySharedFlow()

    override fun initialised() {
        viewModelScope.launch {
            resourceFlowWithRetrying(
                retrySharedFlow = retrySharedFlow,
                logger = logger,
            ) {
                getFlights()
            }.collect {
                updateState {
                    copy(flightsResource = it)
                }
            }
        }
    }

    override fun onNewEvent(event: FlightsEvent) {
        when (event) {
            FlightsEvent.AddFlightClick -> onNavigateToAddLogbook()
            FlightsEvent.RetryClick -> retrySharedFlow.sendRetryEvent()
        }
    }
}
