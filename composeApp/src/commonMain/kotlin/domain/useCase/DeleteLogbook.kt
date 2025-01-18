package domain.useCase

import data.network.AvialogDataProvider
import domain.model.Logbook

class DeleteLogbook(
    private val avialogDataProvider: AvialogDataProvider,
) {
    suspend operator fun invoke(logbook: Logbook) {
        avialogDataProvider.deleteLogbook(logbook = logbook)
    }
}
