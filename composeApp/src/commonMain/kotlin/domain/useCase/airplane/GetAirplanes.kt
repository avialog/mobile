package domain.useCase.airplane

import data.network.AvialogDataProvider
import domain.model.Airplane

class GetAirplanes(
    private val avialogDataProvider: AvialogDataProvider,
) {
    suspend operator fun invoke(): List<Airplane> = avialogDataProvider.getAirplanes()
}
