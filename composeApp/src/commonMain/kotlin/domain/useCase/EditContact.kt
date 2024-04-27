package domain.useCase

import data.network.AvialogDataProvider

class EditContact(
    private val avialogDataProvider: AvialogDataProvider,
) {
    suspend operator fun invoke(
        id: Long,
        firstName: String,
        avatarUrl: String?,
        company: String?,
        emailAddress: String?,
        lastName: String?,
        note: String?,
        phone: String?,
    ) {
        avialogDataProvider.editContact(
            avatarUrl = avatarUrl,
            company = company,
            emailAddress = emailAddress,
            firstName = firstName,
            lastName = lastName,
            note = note,
            phone = phone,
            id = id,
        )
    }
}
