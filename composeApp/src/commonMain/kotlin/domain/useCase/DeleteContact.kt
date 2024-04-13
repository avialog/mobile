package domain.useCase

import data.network.AvialogDataProvider

class DeleteContact(
    private val avialogDataProvider: AvialogDataProvider,
) {
    suspend operator fun invoke(contactId: Long) {
        avialogDataProvider.deleteContact(contactId = contactId)
    }
}
