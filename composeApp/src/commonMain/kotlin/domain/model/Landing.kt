package domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Landing(
    val id: Int,
    val airportCode: String,
    val approachType: ApproachType,
    val dayCount: Long,
    val nightCount: Long,
)
