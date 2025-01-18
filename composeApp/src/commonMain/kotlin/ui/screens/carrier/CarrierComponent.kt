package ui.screens.carrier

import BaseMviViewModel
import ILogger
import RetrySharedFlow
import com.arkivanov.decompose.ComponentContext
import domain.useCase.GenerateReport
import kotlinx.coroutines.launch
import resourceFlowWithRetrying
import savePdf

class CarrierComponent(
    componentContext: ComponentContext,
    private val logger: ILogger,
    private val generateReport: GenerateReport,
) : BaseMviViewModel<CarrierState, CarrierEvent>(
        componentContext = componentContext,
        initialState =
            CarrierState(
                isLoading = true,
                reportResult = null,
            ),
    ) {
    private val retrySharedFlow = RetrySharedFlow()

    override fun onNewEvent(event: CarrierEvent) {
        when (event) {
            CarrierEvent.GenerateRaport -> {
                viewModelScope.launch {
                    resourceFlowWithRetrying(
                        logger = logger,
                        retrySharedFlow = retrySharedFlow,
                    ) {
                        val pdfBytes = generateReport()
                        savePdf(pdfBytes)
                    }.collect {
                        updateState {
                            copy(reportResult = it)
                        }
                    }
                }
            }

            CarrierEvent.RetryClick -> retrySharedFlow.sendRetryEvent()
        }
    }
}
