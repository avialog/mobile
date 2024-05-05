package ui.screens.addLogbook

import domain.model.Landing
import domain.model.Passenger
import domain.model.Style
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

data class AddLogbookState(
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
    val totalBlockTime: DateTimePeriod?,
    val landingAirportCode: String,
    val takeOffAirportCode: String,
    val landingDate: LocalDate?,
    val takeOffDate: LocalDate?,
    val landingTime: LocalTime?,
    val takeOffTime: LocalTime?,
    val personalRemarks: String,
    val remarks: String,
    val landings: List<Landing>,
    val passengers: List<Passenger>,
    val style: Style,
)

sealed interface AddLogbookEvent {
    data object BackClick : AddLogbookEvent

    data object SaveClick : AddLogbookEvent

    data class TakeOffDateChange(val newDate: LocalDate?) : AddLogbookEvent

    data class LandingDateChange(val newDate: LocalDate?) : AddLogbookEvent

    data class TakeOffTimeChange(val newTime: LocalTime?) : AddLogbookEvent

    data class LandingTimeChange(val newTime: LocalTime?) : AddLogbookEvent
}
