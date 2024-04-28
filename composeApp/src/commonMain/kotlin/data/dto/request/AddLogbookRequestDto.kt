package data.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddLogbookRequestDto(
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
)

@Serializable
data class LandingEntryDto(
    @SerialName("airport_code") val airportCode: String,
    @SerialName("approach_type") val approachType: ApproachTypeDto,
    @SerialName("count") val count: Long,
    @SerialName("day_count") val dayCount: Long,
    @SerialName("night_count") val nightCount: Long,
)

@Serializable
data class PassengerEntryDto(
    @SerialName("company") val company: String?,
    @SerialName("email_address") val emailAddress: String?,
    @SerialName("first_name") val firstName: String,
    @SerialName("last_name") val lastName: String?,
    @SerialName("note") val note: String?,
    @SerialName("phone") val phone: String?,
    @SerialName("role") val role: RoleDto,
)

@Serializable
enum class RoleDto {
    PIC,
    SIC,
    DUAL,
    SPIC,
    P1S,
    INS,
    EXM,
    ATT,
    OTH,
}

@Serializable
enum class StyleDto {
    VFR,
    IFR,
    Y,
    Z,
    Z2,
}

@Serializable
enum class ApproachTypeDto {
    VISUAL,
}
