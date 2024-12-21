package ui.screens.chooseContact

import BaseMviViewModel
import ILogger
import RetrySharedFlow
import com.arkivanov.decompose.ComponentContext
import domain.model.Contact
import domain.useCase.GetContacts
import kotlinx.coroutines.launch
import resourceFlowWithRetrying

class ChooseContactComponent(
    componentContext: ComponentContext,
    private val onNavigateBack: () -> Unit,
    private val getContacts: GetContacts,
    private val logger: ILogger,
    private val onChooseContact: (Contact) -> Unit,
) : BaseMviViewModel<ChooseContactState, ChooseContactEvent>(
        componentContext = componentContext,
        initialState =
            ChooseContactState(
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
                getContacts().sortedBy { it.firstName.uppercase() }
            }.collect {
                updateState {
                    copy(contactsResource = it)
                }
            }
        }
    }

    override fun onNewEvent(event: ChooseContactEvent) {
        when (event) {
            ChooseContactEvent.BackClick -> onNavigateBack()
            ChooseContactEvent.RetryClick -> retrySharedFlow.sendRetryEvent()
            is ChooseContactEvent.ContactClick -> onChooseContact(event.contact)
        }
    }
}
