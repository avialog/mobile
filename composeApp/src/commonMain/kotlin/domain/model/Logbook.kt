package domain.model

import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable

@Serializable
data class Logbook(
    val flightId: Long?,
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
    val landingDate: LocalDate,
    val takeOffDate: LocalDate,
    val landingTime: LocalTime,
    val takeOffTime: LocalTime,
    val personalRemarks: String,
    val remarks: String,
    val landings: List<Landing>,
    val passengers: List<Passenger>,
    val style: Style,
    val airplane: Airplane,
    val myRole: Role,
)
