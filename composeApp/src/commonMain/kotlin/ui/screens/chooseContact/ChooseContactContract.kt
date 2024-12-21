package ui.screens.chooseContact

import Resource
import domain.model.Contact

data class ChooseContactState(
    val contactsResource: Resource<List<Contact>>,
)

sealed interface ChooseContactEvent {
    data object BackClick : ChooseContactEvent

    data object RetryClick : ChooseContactEvent

    data class ContactClick(
        val contact: Contact,
    ) : ChooseContactEvent
}
