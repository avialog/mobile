package ui.screens.chooseAirplane

import Resource
import domain.model.Airplane

data class ChooseAirplaneState(
    val airplanesResource: Resource<List<Airplane>>,
)

sealed interface ChooseAirplaneEvent {
    data object BackClick : ChooseAirplaneEvent

    data class AirplaneClick(
        val airplane: Airplane,
    ) : ChooseAirplaneEvent

    data object RetryClick : ChooseAirplaneEvent
}
