package ui.screens.addContact

import BaseMviViewModel
import ILogger
import com.arkivanov.decompose.ComponentContext
import domain.model.Contact
import domain.useCase.AddContact
import domain.useCase.EditContact
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AddContactComponent(
    componentContext: ComponentContext,
    contactToUpdateOrNull: Contact?,
    private val addContact: AddContact,
    private val editContact: EditContact,
    private val logger: ILogger,
    private val onNavigateBack: () -> Unit,
    private val onNavigateBackWithRefreshing: () -> Unit,
) : BaseMviViewModel<AddContactState, AddContactEvent>(
        componentContext = componentContext,
        initialState =
            AddContactState(
                idToUpdateOrNull = contactToUpdateOrNull?.id,
                name = contactToUpdateOrNull?.firstName ?: "",
                surname = contactToUpdateOrNull?.lastName ?: "",
                phoneNumber = contactToUpdateOrNull?.phone ?: "",
                email = contactToUpdateOrNull?.emailAddress ?: "",
                companyName = contactToUpdateOrNull?.company ?: "",
                notes = contactToUpdateOrNull?.note ?: "",
                isRequestInProgress = false,
                showErrorIfAny = false,
            ),
    ) {
    private val errorNotificationChannel = Channel<Unit>(Channel.UNLIMITED)
    val errorNotificationFlow = errorNotificationChannel.receiveAsFlow()

    override fun onNewEvent(event: AddContactEvent) {
        when (event) {
            AddContactEvent.BackClick -> onNavigateBack()
            is AddContactEvent.CompanyNameChange -> {
                updateState {
                    copy(companyName = event.newValue)
                }
            }
            is AddContactEvent.EmailChange -> {
                updateState {
                    copy(email = event.newValue)
                }
            }
            is AddContactEvent.NameChange -> {
                updateState {
                    copy(name = event.newValue)
                }
            }
            is AddContactEvent.NotesChange -> {
                updateState {
                    copy(notes = event.newValue)
                }
            }
            is AddContactEvent.PhoneNumberChange -> {
                updateState {
                    copy(phoneNumber = event.newValue)
                }
            }
            is AddContactEvent.SurnameChange -> {
                updateState {
                    copy(surname = event.newValue)
                }
            }

            AddContactEvent.SaveClick -> {
                if (actualState.name.isBlank()) {
                    updateState {
                        copy(showErrorIfAny = true)
                    }
                    return
                }
                updateState {
                    copy(isRequestInProgress = true)
                }
                viewModelScope.launch {
                    makeAddOrUpdateRequest()
                        .onFailure {
                            coroutineContext.ensureActive()
                            logger.w(it)
                            errorNotificationChannel.send(Unit)
                        }.onSuccess {
                            onNavigateBackWithRefreshing()
                        }
                    updateState {
                        copy(isRequestInProgress = false)
                    }
                }
            }
        }
    }

    private suspend fun makeAddOrUpdateRequest(): Result<Unit> {
        val state = actualState
        return runCatching {
            val firstName = state.name
            val avatarUrl = null
            val company = state.companyName.ifBlank { null }
            val emailAddress = state.email.ifBlank { null }
            val lastName = state.surname.ifBlank { null }
            val notes = state.notes.ifBlank { null }
            val phone = state.phoneNumber.ifBlank { null }
            if (state.idToUpdateOrNull != null) {
                editContact(
                    firstName = firstName,
                    avatarUrl = avatarUrl,
                    company = company,
                    emailAddress = emailAddress,
                    lastName = lastName,
                    note = notes,
                    phone = phone,
                    id = state.idToUpdateOrNull,
                )
            } else {
                addContact(
                    firstName = firstName,
                    avatarUrl = avatarUrl,
                    company = company,
                    emailAddress = emailAddress,
                    lastName = lastName,
                    note = notes,
                    phone = phone,
                )
            }
        }
    }
}
