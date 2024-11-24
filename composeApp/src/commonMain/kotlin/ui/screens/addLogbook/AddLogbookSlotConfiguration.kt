package ui.screens.addLogbook

import domain.model.Role
import kotlinx.serialization.Serializable

@Serializable
sealed interface AddLogbookSlotConfiguration {
    @Serializable
    data object ChooseAirplaneConfiguration : AddLogbookSlotConfiguration

    @Serializable
    data class ChooseContactConfiguration(
        val role: Role,
    ) : AddLogbookSlotConfiguration
}
