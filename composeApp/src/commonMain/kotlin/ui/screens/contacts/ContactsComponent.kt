package ui.screens.contacts

import BaseMviViewModel
import ILogger
import Resource
import RetrySharedFlow
import com.arkivanov.decompose.ComponentContext
import domain.useCase.DeleteContact
import domain.useCase.GetContacts
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import mapIfSuccess
import resourceFlowWithRetrying

class ContactsComponent(
    componentContext: ComponentContext,
    private val onNavigateBack: () -> Unit,
    private val getContacts: GetContacts,
    private val deleteContact: DeleteContact,
    private val logger: ILogger,
    private val onNavigateToAddContact: () -> Unit,
) : BaseMviViewModel<ContactsState, ContactsEvent>(
        componentContext = componentContext,
        initialState =
            ContactsState(
                contactsResource = Resource.Loading,
                isRequestInProgress = false,
            ),
    ) {
    private val retrySharedFlow = RetrySharedFlow()

    override fun initialised() {
        viewModelScope.launch {
            resourceFlowWithRetrying(
                retrySharedFlow = retrySharedFlow,
                logger = logger,
            ) {
                getContacts().sortedBy { it.firstName }
            }.collect {
                updateState {
                    copy(contactsResource = it)
                }
            }
        }
    }

    override fun onNewEvent(event: ContactsEvent) {
        when (event) {
            ContactsEvent.BackClick -> onNavigateBack()
            ContactsEvent.RetryClick -> retrySharedFlow.sendRetryEvent()
            is ContactsEvent.ContactClick -> {}
            ContactsEvent.AddContactClick -> onNavigateToAddContact()
            is ContactsEvent.DeleteContactClick -> {
                updateState {
                    copy(isRequestInProgress = true)
                }
                viewModelScope.launch {
                    runCatching {
                        deleteContact(contactId = event.contact.id)
                    }.onFailure {
                        ensureActive()
                        logger.w(it)
                    }.onSuccess {
                        updateState {
                            copy(
                                contactsResource =
                                    contactsResource.mapIfSuccess { contacts ->
                                        contacts.filterNot { it.id == event.contact.id }
                                    },
                            )
                        }
                    }
                    updateState {
                        copy(isRequestInProgress = false)
                    }
                }
            }
            is ContactsEvent.EditContactClick -> {}
        }
    }
}
