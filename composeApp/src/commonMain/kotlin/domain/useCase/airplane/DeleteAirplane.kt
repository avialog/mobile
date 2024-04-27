package domain.useCase.airplane

import data.network.AvialogDataProvider

class DeleteAirplane(
    private val avialogDataProvider: AvialogDataProvider,
) {
    suspend operator fun invoke(airplaneId: Long) = avialogDataProvider.deleteAirplane(airplaneId = airplaneId)
}
