package domain.useCase

import data.network.AvialogDataProvider

class GenerateReport(
    private val avialogDataProvider: AvialogDataProvider,
) {
    suspend operator fun invoke(): ByteArray = avialogDataProvider.genereteRaport()
}
