package data.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddAirplaneRequestDto(
    @SerialName("aircraft_model") val airplaneModel: String,
    @SerialName("registration_number") val registrationNumber: String,
    @SerialName("remarks") val remarks: String?,
    @SerialName("image_url") val imageUrl: String?,
)
