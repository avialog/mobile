package domain.useCase.airplane

import data.network.AvialogDataProvider

class EditAirplane(
    private val avialogDataProvider: AvialogDataProvider,
) {
    suspend operator fun invoke(
        airplaneId: Long,
        airplaneModel: String,
        registrationNumber: String,
        remarks: String?,
        imageUrl: String?,
    ) = avialogDataProvider.editAirplane(
        airplaneModel = airplaneModel,
        registrationNumber = registrationNumber,
        remarks = remarks,
        imageUrl = imageUrl,
        airplaneId = airplaneId,
    )
}
