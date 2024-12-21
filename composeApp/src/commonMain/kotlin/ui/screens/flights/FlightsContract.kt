package ui.screens.flights

data class FlightsState(
    val token: String?,
)

sealed interface FlightsEvent {
    data object AddFlightClick : FlightsEvent
}
