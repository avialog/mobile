package domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Passenger(
    val company: String?,
    val emailAddress: String?,
    val firstName: String,
    val lastName: String?,
    val note: String?,
    val phone: String?,
    val role: Role,
)

val Passenger.fullName get() = "$firstName ${lastName ?: ""}".trim()
