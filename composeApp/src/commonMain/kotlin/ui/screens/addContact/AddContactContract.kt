package ui.screens.addContact

data class AddContactState(
    val idToUpdateOrNull: Long?,
    val name: String,
    val surname: String,
    val phoneNumber: String,
    val email: String,
    val companyName: String,
    val notes: String,
    val isRequestInProgress: Boolean,
    val showErrorIfAny: Boolean,
)

sealed interface AddContactEvent {
    data object BackClick : AddContactEvent

    data class NameChange(val newValue: String) : AddContactEvent

    data class SurnameChange(val newValue: String) : AddContactEvent

    data class PhoneNumberChange(val newValue: String) : AddContactEvent

    data class EmailChange(val newValue: String) : AddContactEvent

    data class CompanyNameChange(val newValue: String) : AddContactEvent

    data class NotesChange(val newValue: String) : AddContactEvent

    data object SaveClick : AddContactEvent
}
