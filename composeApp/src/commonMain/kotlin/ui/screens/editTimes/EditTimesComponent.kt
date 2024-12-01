package ui.screens.editTimes

import BaseMviViewModel
import com.arkivanov.decompose.ComponentContext
import ui.screens.addLogbook.AddLogbookSlotConfiguration

class EditTimesComponent(
    componentContext: ComponentContext,
    configuration: AddLogbookSlotConfiguration.ChooseTimesConfiguration,
    private val onNavigateBack: (EditTimesState) -> Unit,
) : BaseMviViewModel<EditTimesState, EditTimesEvent>(
        componentContext = componentContext,
        initialState =
            with(configuration) {
                EditTimesState(
                    crossCountryTime = crossCountryTime,
                    dualGivenTime = dualGivenTime,
                    dualReceivedTime = dualReceivedTime,
                    ifrActualTime = ifrActualTime,
                    ifrSimulatedTime = ifrSimulatedTime,
                    ifrTime = ifrTime,
                    nightTime = nightTime,
                    pilotInCommandTime = pilotInCommandTime,
                    secondInCommandTime = secondInCommandTime,
                    simulatorTime = simulatorTime,
                    totalBlockTime = totalBlockTime,
                )
            },
    ) {
    override fun initialised() {
    }

    override fun onNewEvent(event: EditTimesEvent) {
        when (event) {
            is EditTimesEvent.CrossCountryChange -> {
                updateState {
                    copy(crossCountryTime = event.crossCountryTime)
                }
            }
            is EditTimesEvent.DualGivenChange -> {
                updateState {
                    copy(dualGivenTime = event.dualGivenTime)
                }
            }
            is EditTimesEvent.DualReceivedChange -> {
                updateState {
                    copy(dualReceivedTime = event.dualReceivedTime)
                }
            }
            is EditTimesEvent.IfrActualChange -> {
                updateState {
                    copy(ifrActualTime = event.ifrActualTime)
                }
            }
            is EditTimesEvent.IfrSimulatedChange -> {
                updateState {
                    copy(ifrSimulatedTime = event.ifrSimulatedTime)
                }
            }
            is EditTimesEvent.IfrTimeChange -> {
                updateState {
                    copy(ifrTime = event.ifrTime)
                }
            }
            is EditTimesEvent.NightChange -> {
                updateState {
                    copy(nightTime = event.nightTime)
                }
            }
            is EditTimesEvent.PilotInCommandChange -> {
                updateState {
                    copy(pilotInCommandTime = event.pilotInCommandTime)
                }
            }
            is EditTimesEvent.SecondInCommandChange -> {
                updateState {
                    copy(secondInCommandTime = event.secondInCommandTime)
                }
            }
            is EditTimesEvent.SimulatorChange -> {
                updateState {
                    copy(simulatorTime = event.simulatorTime)
                }
            }
            is EditTimesEvent.TotalBlockChange -> {
                updateState {
                    copy(totalBlockTime = event.totalBlockTime)
                }
            }
            EditTimesEvent.SaveClick -> {
                onNavigateBack(actualState)
            }
        }
    }
}
