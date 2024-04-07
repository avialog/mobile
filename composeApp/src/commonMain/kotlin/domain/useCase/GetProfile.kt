package domain.useCase

import data.network.AvialogDataProvider
import domain.model.Profile

class GetProfile(
    private val avialogDataProvider: AvialogDataProvider,
) {
    suspend operator fun invoke(): Profile = avialogDataProvider.getProfile()
}
