package ui.screens.addLogbook

import BaseMviViewModel
import ILogger
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import domain.model.Style
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import ui.screens.chooseAirplane.ChooseAirplaneComponent
import ui.screens.chooseAirplane.ChooseAirplaneConfiguration

class AddLogbookComponent(
    componentContext: ComponentContext,
    private val logger: ILogger,
    private val onNavigateBack: () -> Unit,
) : BaseMviViewModel<AddLogbookState, AddLogbookEvent>(
        componentContext = componentContext,
        initialState =
            AddLogbookState(
                crossCountryTime = null,
                dualGivenTime = null,
                dualReceivedTime = null,
                ifrActualTime = null,
                ifrSimulatedTime = null,
                ifrTime = null,
                nightTime = null,
                pilotInCommandTime = null,
                secondInCommandTime = null,
                simulatorTime = null,
                totalBlockTime = null,
                landingAirportCode = "",
                landingTime = null,
                landingDate = null,
                takeOffDate = null,
                personalRemarks = "",
                remarks = "",
                takeOffAirportCode = "",
                takeOffTime = null,
                landings = listOf(),
                passengers = listOf(),
                style = Style.IFR,
            ),
    ) {
    private val chooseAirplaneSlotNavigation = SlotNavigation<ChooseAirplaneConfiguration>()
    val chooseAirplaneSlot =
        childSlot(
            source = chooseAirplaneSlotNavigation,
            serializer = ChooseAirplaneConfiguration.serializer(),
            handleBackButton = true,
        ) { _, childComponentContext ->
            ChooseAirplaneComponent(componentContext = childComponentContext)
        }

    private val errorNotificationChannel = Channel<Unit>(Channel.UNLIMITED)
    val errorNotificationFlow = errorNotificationChannel.receiveAsFlow()

    override fun onNewEvent(event: AddLogbookEvent) {
        when (event) {
            AddLogbookEvent.BackClick -> onNavigateBack()

            AddLogbookEvent.SaveClick -> {
            }

            is AddLogbookEvent.LandingDateChange -> {
                updateState {
                    copy(landingDate = event.newDate)
                }
            }
            is AddLogbookEvent.TakeOffDateChange -> {
                updateState {
                    copy(takeOffDate = event.newDate)
                }
            }

            is AddLogbookEvent.LandingTimeChange -> {
                updateState {
                    copy(landingTime = event.newTime)
                }
            }
            is AddLogbookEvent.TakeOffTimeChange -> {
                updateState {
                    copy(takeOffTime = event.newTime)
                }
            }

            is AddLogbookEvent.LandingAirportChange -> {
                updateState {
                    copy(landingAirportCode = event.newAirport)
                }
            }
            is AddLogbookEvent.TakeOffAirportChange -> {
                updateState {
                    copy(takeOffAirportCode = event.newAirport)
                }
            }

            AddLogbookEvent.ChooseAirplaneClick -> {
                chooseAirplaneSlotNavigation.activate(ChooseAirplaneConfiguration)
            }
        }
    }
}
