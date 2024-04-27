package domain.useCase.airplane

import data.network.AvialogDataProvider

class AddAirplane(
    private val avialogDataProvider: AvialogDataProvider,
) {
    suspend operator fun invoke(
        airplaneModel: String,
        registrationNumber: String,
        remarks: String?,
        imageUrl: String?,
    ) = avialogDataProvider.addAirplane(
        airplaneModel = airplaneModel,
        registrationNumber = registrationNumber,
        remarks = remarks,
        imageUrl = imageUrl,
    )
}
