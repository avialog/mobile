package ui.screens.addContact

import BaseMviViewModel
import ILogger
import com.arkivanov.decompose.ComponentContext
import domain.useCase.AddContact
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch

class AddContactComponent(
    componentContext: ComponentContext,
    private val addContact: AddContact,
    private val logger: ILogger,
    private val onNavigateBack: () -> Unit,
    private val onNavigateBackWithRefreshing: () -> Unit,
) : BaseMviViewModel<AddContactState, AddContactEvent>(
        componentContext = componentContext,
        initialState =
            AddContactState(
                name = "",
                surname = "",
                phoneNumber = "",
                email = "",
                companyName = "",
                notes = "",
                isRequestInProgress = false,
            ),
    ) {
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
                updateState {
                    copy(isRequestInProgress = true)
                }
                viewModelScope.launch {
                    val state = actualState
                    runCatching {
                        addContact(
                            firstName = state.name,
                            avatarUrl = "",
                            company = state.companyName,
                            emailAddress = state.email,
                            lastName = state.surname,
                            note = state.notes,
                            phone = state.phoneNumber,
                        )
                    }.onFailure {
                        ensureActive()
                        logger.w(it)
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
}
