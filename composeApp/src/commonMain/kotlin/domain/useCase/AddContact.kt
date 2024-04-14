package domain.useCase

import data.network.AvialogDataProvider

class AddContact(
    private val avialogDataProvider: AvialogDataProvider,
) {
    suspend operator fun invoke(
        firstName: String,
        avatarUrl: String?,
        company: String?,
        emailAddress: String?,
        lastName: String?,
        note: String?,
        phone: String?,
    ) {
        avialogDataProvider.addContact(
            avatarUrl = avatarUrl,
            company = company,
            emailAddress = emailAddress,
            firstName = firstName,
            lastName = lastName,
            note = note,
            phone = phone,
        )
    }
}
