package ui.screens.carrier

import BaseMviViewModel
import com.arkivanov.decompose.ComponentContext

class CarrierComponent(
    componentContext: ComponentContext,
) : BaseMviViewModel<CarrierState, CarrierEvent>(
        componentContext = componentContext,
        initialState = CarrierState(isLoading = true),
    ) {
    override fun onNewEvent(event: CarrierEvent) {
    }
}
