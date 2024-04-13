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
}
