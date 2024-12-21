package ui.screens.flights

import Resource
import domain.model.Logbook

data class FlightsState(
    val flightsResource: Resource<List<Logbook>>,
    val requestInProgress: Boolean,
)

sealed interface FlightsEvent {
    data object AddFlightClick : FlightsEvent

    data class EditFlightClick(
        val logbook: Logbook,
    ) : FlightsEvent

    data class RemoveFlightClick(
        val logbook: Logbook,
    ) : FlightsEvent

    data object RetryClick : FlightsEvent
}
