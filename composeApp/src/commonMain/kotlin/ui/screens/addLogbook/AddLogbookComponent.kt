package ui.screens.addLogbook

import BaseMviViewModel
import ILogger
import Resource
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import di.di
import domain.model.ApproachType
import domain.model.Contact
import domain.model.Landing
import domain.model.Logbook
import domain.model.Passenger
import domain.model.Role
import domain.model.Style
import domain.useCase.AddLogbook
import domain.useCase.EditLogbook
import domain.useCase.GetContacts
import domain.useCase.airplane.GetAirplanes
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimePeriod
import org.kodein.di.instance
import resourceFlow
import ui.screens.chooseAirplane.ChooseAirplaneComponent
import ui.screens.chooseContact.ChooseContactComponent
import ui.screens.editTimes.EditTimesComponent

private const val MAX_AIRPORT_CODE_LENGTH = 5

class AddLogbookComponent(
    private val logbookToUpdateOrNull: Logbook?,
    componentContext: ComponentContext,
    private val logger: ILogger,
    private val addLogbook: AddLogbook,
    private val editLogbook: EditLogbook,
    private val onNavigateBack: () -> Unit,
) : BaseMviViewModel<AddLogbookState, AddLogbookEvent>(
        componentContext = componentContext,
        initialState =
            AddLogbookState(
                crossCountryTime = logbookToUpdateOrNull?.crossCountryTime,
                dualGivenTime = logbookToUpdateOrNull?.dualGivenTime,
                dualReceivedTime = logbookToUpdateOrNull?.dualReceivedTime,
                ifrActualTime = logbookToUpdateOrNull?.ifrActualTime,
                ifrSimulatedTime = logbookToUpdateOrNull?.ifrSimulatedTime,
                ifrTime = logbookToUpdateOrNull?.ifrTime,
                nightTime = logbookToUpdateOrNull?.nightTime,
                pilotInCommandTime = logbookToUpdateOrNull?.pilotInCommandTime,
                secondInCommandTime = logbookToUpdateOrNull?.secondInCommandTime,
                simulatorTime = logbookToUpdateOrNull?.simulatorTime,
                totalBlockTime = logbookToUpdateOrNull?.totalBlockTime ?: DateTimePeriod(),
                landingAirportCode = logbookToUpdateOrNull?.landingAirportCode ?: "",
                landingTime = logbookToUpdateOrNull?.landingTime,
                landingDate = logbookToUpdateOrNull?.landingDate,
                takeOffDate = logbookToUpdateOrNull?.takeOffDate,
                personalRemarks = logbookToUpdateOrNull?.personalRemarks ?: "",
                remarks = logbookToUpdateOrNull?.remarks ?: "",
                takeOffAirportCode = logbookToUpdateOrNull?.takeOffAirportCode ?: "",
                takeOffTime = logbookToUpdateOrNull?.takeOffTime,
                landings =
                    logbookToUpdateOrNull?.landings ?: listOf(
                        Landing(
                            airportCode = "",
                            approachType = ApproachType.VISUAL,
                            dayCount = 0,
                            nightCount = 0,
                            id = 0,
                        ),
                    ),
                passengers = logbookToUpdateOrNull?.passengers ?: listOf(),
                style = logbookToUpdateOrNull?.style ?: Style.IFR,
                airplane = logbookToUpdateOrNull?.airplane,
                myRole = logbookToUpdateOrNull?.myRole ?: Role.PIC,
                requestState = null,
                isEdit = logbookToUpdateOrNull != null,
            ),
    ) {
    private val landingsCreatedCounter = atomic(0)
    private val chooseAirplaneSlotNavigation = SlotNavigation<AddLogbookSlotConfiguration>()

    val addLogbookChildSlot =
        childSlot(
            source = chooseAirplaneSlotNavigation,
            serializer = AddLogbookSlotConfiguration.serializer(),
            handleBackButton = true,
        ) { configuration, childComponentContext ->
            when (configuration) {
                AddLogbookSlotConfiguration.ChooseAirplaneConfiguration -> {
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
                is AddLogbookSlotConfiguration.ChooseContactConfiguration -> {
                    val getContacts: GetContacts by di.instance()
                    ChooseContactComponent(
                        componentContext = childComponentContext,
                        getContacts = getContacts,
                        onNavigateBack = {
                            chooseAirplaneSlotNavigation.dismiss()
                        },
                        logger = logger,
                        onChooseContact = { newContact ->
                            chooseAirplaneSlotNavigation.dismiss()
                            updateState {
                                copy(
                                    passengers =
                                        passengers +
                                            newContact.toPassenger(role = configuration.role),
                                )
                            }
                        },
                    )
                }

                is AddLogbookSlotConfiguration.ChooseTimesConfiguration -> {
                    EditTimesComponent(
                        componentContext = childComponentContext,
                        onNavigateBack = { editTimesState ->
                            chooseAirplaneSlotNavigation.dismiss()
                            updateState {
                                with(editTimesState) {
                                    this@updateState.copy(
                                        crossCountryTime = crossCountryTime,
                                        dualGivenTime = dualGivenTime,
                                        dualReceivedTime = dualReceivedTime,
                                        ifrActualTime = ifrActualTime,
                                        ifrSimulatedTime = ifrSimulatedTime,
                                        ifrTime = ifrTime,
                                        nightTime = nightTime,
                                        pilotInCommandTime = pilotInCommandTime,
                                        secondInCommandTime = secondInCommandTime,
                                        simulatorTime = simulatorTime,
                                        totalBlockTime = totalBlockTime,
                                    )
                                }
                            }
                        },
                        configuration = configuration,
                    )
                }
            }
        }

    private val errorNotificationChannel = Channel<Unit>(Channel.UNLIMITED)

    val errorNotificationFlow = errorNotificationChannel.receiveAsFlow()

    override fun onNewEvent(event: AddLogbookEvent) {
        when (event) {
            AddLogbookEvent.BackClick -> onNavigateBack()

            AddLogbookEvent.SaveClick -> {
                if (actualState.requestState is Resource.Loading) return
                val logbook =
                    kotlin
                        .runCatching {
                            with(actualState) {
                                Logbook(
                                    crossCountryTime = crossCountryTime,
                                    dualGivenTime = dualGivenTime,
                                    dualReceivedTime = dualReceivedTime,
                                    ifrActualTime = ifrActualTime,
                                    ifrSimulatedTime = ifrSimulatedTime,
                                    ifrTime = ifrTime,
                                    nightTime = nightTime,
                                    pilotInCommandTime = pilotInCommandTime,
                                    secondInCommandTime = secondInCommandTime,
                                    simulatorTime = simulatorTime,
                                    totalBlockTime = totalBlockTime,
                                    landingAirportCode = landingAirportCode,
                                    takeOffAirportCode = takeOffAirportCode,
                                    landingDate = landingDate!!,
                                    takeOffDate = takeOffDate!!,
                                    landingTime = landingTime!!,
                                    takeOffTime = takeOffTime!!,
                                    personalRemarks = personalRemarks,
                                    remarks = remarks,
                                    landings = landings,
                                    passengers = passengers,
                                    style = style,
                                    airplane = airplane!!,
                                    myRole = myRole,
                                )
                            }
                        }.onFailure {
                            errorNotificationChannel.trySend(Unit)
                        }.getOrNull()

                logbook?.let {
                    viewModelScope.launch {
                        resourceFlow(
                            logger = logger,
                        ) {
                            if (logbookToUpdateOrNull == null) {
                                addLogbook(logbook = logbook)
                            } else {
                                editLogbook(logbook = logbook)
                            }
                        }.collect {
                            updateState { copy(requestState = it) }
                            if (it is Resource.Success) {
                                onNavigateBack()
                            }
                            if (it is Resource.Error) {
                                errorNotificationChannel.trySend(Unit)
                            }
                        }
                    }
                }
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
                chooseAirplaneSlotNavigation.activate(AddLogbookSlotConfiguration.ChooseAirplaneConfiguration)
            }

            is AddLogbookEvent.LandingChange -> {
                updateState {
                    val newLandings =
                        landings.toMutableList().apply {
                            this[event.index] = event.newLanding
                        }
                    copy(
                        landings = newLandings,
                        landingAirportCode = newLandings.last().airportCode,
                    )
                }
            }

            AddLogbookEvent.AddLandingClick -> {
                updateState {
                    copy(
                        landings =
                            listOf(
                                Landing(
                                    airportCode = "",
                                    approachType = ApproachType.VISUAL,
                                    dayCount = 0,
                                    nightCount = 0,
                                    id = landingsCreatedCounter.incrementAndGet(),
                                ),
                                *landings.toTypedArray(),
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

            is AddLogbookEvent.RemovePassengerClick ->
                updateState {
                    copy(
                        passengers =
                            passengers.toMutableList().apply {
                                removeAt(event.index)
                            },
                    )
                }

            is AddLogbookEvent.AddPassengerClick -> {
                chooseAirplaneSlotNavigation.activate(
                    AddLogbookSlotConfiguration.ChooseContactConfiguration(role = event.role),
                )
            }

            is AddLogbookEvent.ChangeMyRoleClick -> {
                updateState {
                    copy(myRole = event.role)
                }
            }

            is AddLogbookEvent.PersonalRemarksChange ->
                updateState {
                    copy(personalRemarks = event.newRemarks)
                }
            is AddLogbookEvent.RemarksChange ->
                updateState {
                    copy(remarks = event.newRemarks)
                }

            is AddLogbookEvent.StyleChange -> {
                updateState {
                    copy(style = event.style)
                }
            }

            AddLogbookEvent.TimesChangeClick ->
                chooseAirplaneSlotNavigation.activate(
                    with(actualState) {
                        AddLogbookSlotConfiguration.ChooseTimesConfiguration(
                            crossCountryTime = crossCountryTime,
                            dualGivenTime = dualGivenTime,
                            dualReceivedTime = dualReceivedTime,
                            ifrActualTime = ifrActualTime,
                            ifrSimulatedTime = ifrSimulatedTime,
                            ifrTime = ifrTime,
                            nightTime = nightTime,
                            pilotInCommandTime = pilotInCommandTime,
                            secondInCommandTime = secondInCommandTime,
                            simulatorTime = simulatorTime,
                            totalBlockTime = totalBlockTime,
                        )
                    },
                )
        }
    }

    private fun Contact.toPassenger(role: Role): Passenger =
        Passenger(
            firstName = firstName,
            lastName = lastName,
            emailAddress = emailAddress,
            phone = phone,
            company = company,
            note = note,
            role = role,
        )
}
