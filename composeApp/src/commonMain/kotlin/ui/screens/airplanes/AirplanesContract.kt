package ui.screens.airplanes

import Resource
import domain.model.Airplane

data class AirplanesState(
    val airplanesResource: Resource<List<Airplane>>,
    val isRequestInProgress: Boolean,
)

sealed interface AirplanesEvent {
    data object BackClick : AirplanesEvent

    data object AddAirplaneClick : AirplanesEvent

    data object RetryClick : AirplanesEvent

    data class AirplaneClick(val airplane: Airplane) : AirplanesEvent

    data class EditAirplaneClick(val airplane: Airplane) : AirplanesEvent

    data class DeleteAirplaneClick(val airplane: Airplane) : AirplanesEvent
}
