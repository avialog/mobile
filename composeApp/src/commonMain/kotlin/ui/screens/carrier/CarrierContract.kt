package ui.screens.carrier

import Resource

data class CarrierState(
    val isLoading: Boolean,
    val reportResult: Resource<Unit>?,
)

sealed interface CarrierEvent {
    data object GenerateRaport : CarrierEvent

    data object RetryClick : CarrierEvent
}
