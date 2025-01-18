package data.dto.response

import data.dto.request.LandingEntryDto
import data.dto.request.PassengerEntryDto
import data.dto.request.RoleDto
import data.dto.request.StyleDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LogbookResponseDto(
    @SerialName("flight_id") val flightId: Long?,
    @SerialName("cross_country_time") val crossCountryTime: Long?,
    @SerialName("dual_given_time") val dualGivenTime: Long?,
    @SerialName("dual_received_time") val dualReceivedTime: Long?,
    @SerialName("ifr_actual_time") val ifrActualTime: Long?,
    @SerialName("ifr_simulated_time") val ifrSimulatedTime: Long?,
    @SerialName("ifr_time") val ifrTime: Long?,
    @SerialName("night_time") val nightTime: Long?,
    @SerialName("pilot_in_command_time") val pilotInCommandTime: Long?,
    @SerialName("second_in_command_time") val secondInCommandTime: Long?,
    @SerialName("simulator_time") val simulatorTime: Long?,
    @SerialName("total_block_time") val totalBlockTime: Long?,
    @SerialName("landing_airport_code") val landingAirportCode: String,
    @SerialName("landing_time") val landingTime: String,
    @SerialName("personal_remarks") val personalRemarks: String?,
    @SerialName("remarks") val remarks: String?,
    @SerialName("signature_url") val signatureUrl: String?,
    @SerialName("takeoff_airport_code") val takeOffAirportCode: String,
    @SerialName("takeoff_time") val takeOffTime: String,
    @SerialName("landings") val landings: List<LandingEntryDto>,
    @SerialName("passengers") val passengers: List<PassengerEntryDto>,
    @SerialName("style") val style: StyleDto,
    @SerialName("my_role") val myRole: RoleDto,
    @SerialName("aircraft_id") val airplaneId: Long,
)
