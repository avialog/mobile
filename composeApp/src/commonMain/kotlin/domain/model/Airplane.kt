package domain.model

data class Airplane(
    val id: Long,
    val airplaneModel: String,
    val registrationNumber: String,
    val remarks: String?,
    val imageUrl: String,
)
