package ui.screens.carrier

data class CarrierState(
    val isLoading: Boolean,
)

sealed interface CarrierEvent
