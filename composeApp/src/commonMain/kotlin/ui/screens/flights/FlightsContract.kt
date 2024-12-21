package ui.screens.flights

import Resource
import domain.model.Logbook

data class FlightsState(
    val flightsResource: Resource<List<Logbook>>,
)

sealed interface FlightsEvent {
    data object AddFlightClick : FlightsEvent

    data object RetryClick : FlightsEvent
}
