package ui.screens.editTimes

import kotlinx.datetime.DateTimePeriod

data class EditTimesState(
    val crossCountryTime: DateTimePeriod?,
    val dualGivenTime: DateTimePeriod?,
    val dualReceivedTime: DateTimePeriod?,
    val ifrActualTime: DateTimePeriod?,
    val ifrSimulatedTime: DateTimePeriod?,
    val ifrTime: DateTimePeriod?,
    val nightTime: DateTimePeriod?,
    val pilotInCommandTime: DateTimePeriod?,
    val secondInCommandTime: DateTimePeriod?,
    val simulatorTime: DateTimePeriod?,
    val totalBlockTime: DateTimePeriod,
)

sealed interface EditTimesEvent {
    data class CrossCountryChange(
        val crossCountryTime: DateTimePeriod?,
    ) : EditTimesEvent

    data class DualGivenChange(
        val dualGivenTime: DateTimePeriod?,
    ) : EditTimesEvent

    data class DualReceivedChange(
        val dualReceivedTime: DateTimePeriod?,
    ) : EditTimesEvent

    data class IfrActualChange(
        val ifrActualTime: DateTimePeriod?,
    ) : EditTimesEvent

    data class IfrSimulatedChange(
        val ifrSimulatedTime: DateTimePeriod?,
    ) : EditTimesEvent

    data class IfrTimeChange(
        val ifrTime: DateTimePeriod?,
    ) : EditTimesEvent

    data class NightChange(
        val nightTime: DateTimePeriod?,
    ) : EditTimesEvent

    data class PilotInCommandChange(
        val pilotInCommandTime: DateTimePeriod?,
    ) : EditTimesEvent

    data class SecondInCommandChange(
        val secondInCommandTime: DateTimePeriod?,
    ) : EditTimesEvent

    data class SimulatorChange(
        val simulatorTime: DateTimePeriod?,
    ) : EditTimesEvent

    data class TotalBlockChange(
        val totalBlockTime: DateTimePeriod,
    ) : EditTimesEvent

    data object SaveClick : EditTimesEvent
}
