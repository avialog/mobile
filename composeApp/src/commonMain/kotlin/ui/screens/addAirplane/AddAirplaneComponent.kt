package ui.screens.addAirplane

import BaseMviViewModel
import ILogger
import com.arkivanov.decompose.ComponentContext
import domain.model.Airplane
import domain.useCase.airplane.AddAirplane
import domain.useCase.airplane.EditAirplane
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AddAirplaneComponent(
    componentContext: ComponentContext,
    airplaneToUpdateOrNull: Airplane?,
    private val addAirplane: AddAirplane,
    private val editAirplane: EditAirplane,
    private val logger: ILogger,
    private val onNavigateBack: () -> Unit,
    private val onNavigateBackWithRefreshing: () -> Unit,
) : BaseMviViewModel<AddAirplaneState, AddAirplaneEvent>(
        componentContext = componentContext,
        initialState =
            AddAirplaneState(
                idToUpdateOrNull = airplaneToUpdateOrNull?.id,
                airplaneModel = airplaneToUpdateOrNull?.airplaneModel ?: "",
                registrationNumber = airplaneToUpdateOrNull?.registrationNumber ?: "",
                remarks = airplaneToUpdateOrNull?.remarks ?: "",
                isRequestInProgress = false,
                showErrorIfAny = false,
            ),
    ) {
    private val errorNotificationChannel = Channel<Unit>(Channel.UNLIMITED)
    val errorNotificationFlow = errorNotificationChannel.receiveAsFlow()

    override fun onNewEvent(event: AddAirplaneEvent) {
        when (event) {
            AddAirplaneEvent.BackClick -> onNavigateBack()
            is AddAirplaneEvent.ModelChange -> {
                updateState {
                    copy(airplaneModel = event.newValue)
                }
            }
            is AddAirplaneEvent.RemarksChange -> {
                updateState {
                    copy(remarks = event.newValue)
                }
            }
            is AddAirplaneEvent.RegistrationNumberChange -> {
                updateState {
                    copy(registrationNumber = event.newValue)
                }
            }

            AddAirplaneEvent.SaveClick -> {
                if (actualState.airplaneModel.isBlank() || actualState.registrationNumber.isBlank()) {
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
            val airplaneModel = state.airplaneModel
            val registrationNumber = state.registrationNumber
            val remarks = state.remarks.ifBlank { null }
            val imageUrl = null
            if (state.idToUpdateOrNull != null) {
                editAirplane(
                    airplaneModel = airplaneModel,
                    registrationNumber = registrationNumber,
                    remarks = remarks,
                    imageUrl = imageUrl,
                    airplaneId = state.idToUpdateOrNull,
                )
            } else {
                addAirplane(
                    airplaneModel = airplaneModel,
                    registrationNumber = registrationNumber,
                    remarks = remarks,
                    imageUrl = imageUrl,
                )
            }
        }
    }
}
