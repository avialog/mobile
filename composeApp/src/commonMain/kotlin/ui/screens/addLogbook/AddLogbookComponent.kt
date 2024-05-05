package ui.screens.addLogbook

import BaseMviViewModel
import ILogger
import com.arkivanov.decompose.ComponentContext
import domain.model.Style
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

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
        }
    }
}
