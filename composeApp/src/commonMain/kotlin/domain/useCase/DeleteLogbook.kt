package domain.useCase

import data.network.AvialogDataProvider
import domain.model.Logbook
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

class DeleteLogbook(
    private val avialogDataProvider: AvialogDataProvider,
) {
    suspend operator fun invoke(logbook: Logbook) {
        delay(2.seconds)
    }
}
