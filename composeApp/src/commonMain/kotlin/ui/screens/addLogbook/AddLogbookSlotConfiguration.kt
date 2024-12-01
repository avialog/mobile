package ui.screens.addLogbook

import domain.model.Role
import kotlinx.datetime.DateTimePeriod
import kotlinx.serialization.Serializable

@Serializable
sealed interface AddLogbookSlotConfiguration {
    @Serializable
    data object ChooseAirplaneConfiguration : AddLogbookSlotConfiguration

    @Serializable
    data class ChooseContactConfiguration(
        val role: Role,
    ) : AddLogbookSlotConfiguration

    @Serializable
    data class ChooseTimesConfiguration(
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
    ) : AddLogbookSlotConfiguration
}
