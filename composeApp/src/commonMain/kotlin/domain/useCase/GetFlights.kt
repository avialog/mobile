package domain.useCase

import data.network.AvialogDataProvider
import domain.model.Logbook

class GetFlights(
    private val avialogDataProvider: AvialogDataProvider,
) {
    suspend operator fun invoke(): List<Logbook> = avialogDataProvider.getFlights()
}
