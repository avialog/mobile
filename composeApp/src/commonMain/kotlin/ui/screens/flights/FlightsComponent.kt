package ui.screens.flights

import BaseMviViewModel
import ILogger
import Resource
import RetrySharedFlow
import com.arkivanov.decompose.ComponentContext
import domain.model.Logbook
import domain.useCase.DeleteLogbook
import domain.useCase.GetFlights
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import mapIfSuccess
import resourceFlowWithRetrying

class FlightsComponent(
    componentContext: ComponentContext,
    private val onNavigateToAddLogbook: () -> Unit,
    private val onNavigateToEditLogbook: (Logbook) -> Unit,
    private val getFlights: GetFlights,
    private val deleteLogbook: DeleteLogbook,
    private val logger: ILogger,
) : BaseMviViewModel<FlightsState, FlightsEvent>(
        componentContext = componentContext,
        initialState =
            FlightsState(
                flightsResource = Resource.Loading,
                requestInProgress = false,
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
            is FlightsEvent.EditFlightClick -> onNavigateToEditLogbook(event.logbook)
            is FlightsEvent.RemoveFlightClick -> {
                viewModelScope.launch {
                    updateState {
                        copy(requestInProgress = true)
                    }
                    runCatching {
                        deleteLogbook(logbook = event.logbook)
                    }.onFailure {
                        ensureActive()
                        logger.w(it)
                    }.onSuccess {
                        updateState {
                            copy(
                                requestInProgress = false,
                                flightsResource =
                                    flightsResource.mapIfSuccess {
                                        it.filterNot { it == event.logbook }
                                    },
                            )
                        }
                    }
                }
            }
        }
    }
}
