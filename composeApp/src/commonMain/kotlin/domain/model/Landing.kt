package domain.model

data class Landing(
    val airportCode: String,
    val approachType: ApproachType,
    val count: Long,
    val dayCount: Long,
    val nightCount: Long,
)
