package ui.screens.contacts

import BaseMviViewModel
import ILogger
import Resource
import RetrySharedFlow
import com.arkivanov.decompose.ComponentContext
import domain.useCase.GetContacts
import kotlinx.coroutines.launch
import resourceFlowWithRetrying

class ContactsComponent(
    componentContext: ComponentContext,
    private val onNavigateBack: () -> Unit,
    private val getContacts: GetContacts,
    private val logger: ILogger,
) : BaseMviViewModel<ContactsState, ContactsEvent>(
        componentContext = componentContext,
        initialState =
            ContactsState(
                contactsResource = Resource.Loading,
            ),
    ) {
    private val retrySharedFlow = RetrySharedFlow()

    override fun initialised() {
        viewModelScope.launch {
            resourceFlowWithRetrying(
                retrySharedFlow = retrySharedFlow,
                logger = logger,
            ) {
                getContacts()
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
        }
    }
}
