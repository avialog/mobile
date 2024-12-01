package domain.useCase

import data.network.AvialogDataProvider
import domain.model.Logbook

class AddLogbook(
    private val avialogDataProvider: AvialogDataProvider,
) {
    suspend operator fun invoke(logbook: Logbook) {
        avialogDataProvider.addLogbook(logbook)
    }
}
