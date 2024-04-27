package ui.screens.addAirplane

data class AddAirplaneState(
    val idToUpdateOrNull: Long?,
    val airplaneModel: String,
    val registrationNumber: String,
    val remarks: String,
    val isRequestInProgress: Boolean,
    val showErrorIfAny: Boolean,
)

sealed interface AddAirplaneEvent {
    data object BackClick : AddAirplaneEvent

    data class ModelChange(val newValue: String) : AddAirplaneEvent

    data class RegistrationNumberChange(val newValue: String) : AddAirplaneEvent

    data class RemarksChange(val newValue: String) : AddAirplaneEvent

    data object SaveClick : AddAirplaneEvent
}
