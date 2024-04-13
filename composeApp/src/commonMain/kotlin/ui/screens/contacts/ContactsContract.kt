package ui.screens.contacts

import Resource
import domain.model.Contact

data class ContactsState(
    val contactsResource: Resource<List<Contact>>,
)

sealed interface ContactsEvent {
    data object BackClick : ContactsEvent

    data object RetryClick : ContactsEvent

    data class ContactClick(val contact: Contact) : ContactsEvent

    data object AddContactClick : ContactsEvent

    data class EditContactClick(val contact: Contact) : ContactsEvent

    data class DeleteContactClick(val contact: Contact) : ContactsEvent
}
