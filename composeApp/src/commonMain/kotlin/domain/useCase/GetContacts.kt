package domain.useCase

import data.network.AvialogDataProvider
import domain.model.Contact

class GetContacts(
    private val avialogDataProvider: AvialogDataProvider,
) {
    suspend operator fun invoke(): List<Contact> = avialogDataProvider.getContacts()
}
