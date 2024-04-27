package data.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EditContactRequestDto(
    @SerialName("avatar_url") val avatarUrl: String?,
    @SerialName("company") val company: String?,
    @SerialName("email_address") val emailAddress: String?,
    @SerialName("first_name") val firstName: String,
    @SerialName("last_name") val lastName: String?,
    @SerialName("note") val note: String?,
    @SerialName("phone")val phone: String?,
)
