package domain.model

data class Passenger(
    val company: String?,
    val emailAddress: String?,
    val firstName: String,
    val lastName: String?,
    val note: String?,
    val phone: String?,
    val role: Role,
)
