package ui.screens.addLogbook

import BaseMviViewModel
import ILogger
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import di.di
import domain.model.ApproachType
import domain.model.Landing
import domain.model.Style
import domain.useCase.airplane.GetAirplanes
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import org.kodein.di.instance
import ui.screens.chooseAirplane.ChooseAirplaneComponent
import ui.screens.chooseAirplane.ChooseAirplaneConfiguration

private const val MAX_AIRPORT_CODE_LENGTH = 5

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
                landings =
                    listOf(
                        Landing(
                            airportCode = "",
                            approachType = ApproachType.VISUAL,
                            count = 0,
                            dayCount = 0,
                            nightCount = 0,
                        ),
                    ),
                passengers = listOf(),
                style = Style.IFR,
                airplane = null,
            ),
    ) {
    private val chooseAirplaneSlotNavigation = SlotNavigation<ChooseAirplaneConfiguration>()
    val chooseAirplaneSlot =
        childSlot(
            source = chooseAirplaneSlotNavigation,
            serializer = ChooseAirplaneConfiguration.serializer(),
            handleBackButton = true,
        ) { _, childComponentContext ->
            val getAirplanes: GetAirplanes by di.instance()
            ChooseAirplaneComponent(
                componentContext = childComponentContext,
                getAirplanes = getAirplanes,
                onAirplaneChosen = {
                    updateState {
                        copy(airplane = it)
                    }
                    chooseAirplaneSlotNavigation.dismiss()
                },
                onNavigateBack = {
                    chooseAirplaneSlotNavigation.dismiss()
                },
                logger = logger,
            )
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
                val newAirport = event.newAirport.take(MAX_AIRPORT_CODE_LENGTH)
                updateState {
                    copy(
                        landingAirportCode = newAirport,
                        landings =
                            landings.toMutableList().apply {
                                this[lastIndex] = last().copy(airportCode = newAirport)
                            },
                    )
                }
            }
            is AddLogbookEvent.TakeOffAirportChange -> {
                updateState {
                    copy(takeOffAirportCode = event.newAirport.take(MAX_AIRPORT_CODE_LENGTH))
                }
            }

            AddLogbookEvent.ChooseAirplaneClick -> {
                chooseAirplaneSlotNavigation.activate(ChooseAirplaneConfiguration)
            }

            is AddLogbookEvent.LandingChange -> {
                updateState {
                    copy(
                        landings =
                            landings.toMutableList().apply {
                                this[event.index] = event.newLanding
                            },
                    )
                }
            }

            AddLogbookEvent.AddLandingClick -> {
                updateState {
                    copy(
                        landings =
                            landings +
                                Landing(
                                    airportCode = "",
                                    approachType = ApproachType.VISUAL,
                                    count = 0,
                                    dayCount = 0,
                                    nightCount = 0,
                                ),
                    )
                }
            }

            is AddLogbookEvent.RemoveLandingClick -> {
                updateState {
                    copy(
                        landings =
                            landings.toMutableList().apply {
                                removeAt(event.index)
                            },
                    )
                }
            }
        }
    }
}
