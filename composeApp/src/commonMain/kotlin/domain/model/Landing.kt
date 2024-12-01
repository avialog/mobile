package domain.model

data class Landing(
    val id: Int,
    val airportCode: String,
    val approachType: ApproachType,
    val dayCount: Long,
    val nightCount: Long,
)
