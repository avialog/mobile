package navigation

import ILogger
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.navigate
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.router.stack.replaceCurrent
import data.repository.auth.IAuthRepository
import di.di
import domain.model.Airplane
import domain.model.Contact
import domain.useCase.AddContact
import domain.useCase.DeleteContact
import domain.useCase.EditContact
import domain.useCase.GetContacts
import domain.useCase.GetProfile
import domain.useCase.IsUserLoggedIn
import domain.useCase.LogOut
import domain.useCase.LoginWithEmailAndPassword
import domain.useCase.RegisterWithEmailAndPassword
import domain.useCase.airplane.AddAirplane
import domain.useCase.airplane.DeleteAirplane
import domain.useCase.airplane.EditAirplane
import domain.useCase.airplane.GetAirplanes
import kotlinx.serialization.Serializable
import org.kodein.di.instance
import ui.screens.addAirplane.AddAirplaneComponent
import ui.screens.addContact.AddContactComponent
import ui.screens.addLogbook.AddLogbookComponent
import ui.screens.airplanes.AirplanesComponent
import ui.screens.carrier.CarrierComponent
import ui.screens.contacts.ContactsComponent
import ui.screens.flights.FlightsComponent
import ui.screens.login.AreLoginInputsValid
import ui.screens.login.LoginComponent
import ui.screens.onboarding.OnboardingComponent
import ui.screens.profile.ProfileComponent

class RootComponent(
    componentContext: ComponentContext,
) : ComponentContext by componentContext {
    val navigation = StackNavigation<Configuration>()
    val childStack =
        childStack(
            source = navigation,
            serializer = Configuration.serializer(),
            initialConfiguration = Configuration.Onboarding,
            handleBackButton = true,
            childFactory = ::createChild,
        )

    @OptIn(ExperimentalDecomposeApi::class)
    private fun createChild(
        config: Configuration,
        context: ComponentContext,
    ): Child {
        return when (config) {
            Configuration.Login -> {
                val loginWithEmailAndPassword: LoginWithEmailAndPassword by di.instance()
                val registerWithEmailAndPassword: RegisterWithEmailAndPassword by di.instance()
                val areLoginInputsValid: AreLoginInputsValid by di.instance()
                val logger: ILogger by di.instance()

                Child.Login(
                    LoginComponent(
                        componentContext = context,
                        onNavigateToHome = {
                            navigation.navigate { _ ->
                                listOf(Configuration.Flights)
                            }
                        },
                        loginWithEmailAndPassword = loginWithEmailAndPassword,
                        registerWithEmailAndPassword = registerWithEmailAndPassword,
                        areLoginInputsValid = areLoginInputsValid,
                        logger = logger,
                    ),
                )
            }

            Configuration.Flights -> {
                val authRepository by di.instance<IAuthRepository>()
                Child.Flights(
                    component =
                        FlightsComponent(
                            componentContext = context,
                            authRepository = authRepository,
                            onNavigateToAddLogbook = {
                                navigation.pushNew(Configuration.AddLogbook)
                            },
                        ),
                )
            }

            Configuration.Carrier -> {
                Child.Carrier(
                    component =
                        CarrierComponent(
                            componentContext = context,
                        ),
                )
            }
            Configuration.Profile -> {
                val getProfile: GetProfile by di.instance()
                val logger: ILogger by di.instance()
                val logOut: LogOut by di.instance()
                Child.Profile(
                    component =
                        ProfileComponent(
                            componentContext = context,
                            logger = logger,
                            getProfile = getProfile,
                            logOut = logOut,
                            onNavigateToLogin = {
                                navigation.replaceAll(Configuration.Login)
                            },
                            onNavigateToContacts = {
                                navigation.pushNew(Configuration.Contacts)
                            },
                            onNavigateToAirplanes = {
                                navigation.pushNew(Configuration.Airplanes)
                            },
                        ),
                )
            }

            Configuration.Onboarding -> {
                val isUserLoggerIn: IsUserLoggedIn by di.instance()

                Child.Onboarding(
                    component =
                        OnboardingComponent(
                            componentContext = context,
                            onNavigateToHome = {
                                navigation.replaceCurrent(Configuration.Flights)
                            },
                            isUserLoggedIn = isUserLoggerIn,
                            onNavigateToLogin = {
                                navigation.pushNew(Configuration.Login)
                            },
                        ),
                )
            }

            Configuration.Contacts -> {
                val getContacts: GetContacts by di.instance()
                val deleteContact: DeleteContact by di.instance()
                val logger: ILogger by di.instance()

                Child.Contacts(
                    component =
                        ContactsComponent(
                            componentContext = context,
                            getContacts = getContacts,
                            logger = logger,
                            deleteContact = deleteContact,
                            onNavigateBack = {
                                navigation.pop()
                            },
                            onNavigateToAddContact = {
                                navigation.pushNew(Configuration.AddContact())
                            },
                            onNavigateToUpdateContact = {
                                navigation.pushNew(
                                    Configuration.AddContact(contactToUpdateOrNull = it),
                                )
                            },
                        ),
                )
            }

            is Configuration.AddContact -> {
                val logger: ILogger by di.instance()
                val addContact: AddContact by di.instance()
                val editContact: EditContact by di.instance()

                Child.AddContact(
                    component =
                        AddContactComponent(
                            componentContext = context,
                            addContact = addContact,
                            editContact = editContact,
                            logger = logger,
                            onNavigateBack = {
                                navigation.pop()
                            },
                            onNavigateBackWithRefreshing = {
                                navigation.pop()
                                navigation.pop()
                                navigation.pushNew(Configuration.Contacts)
                            },
                            contactToUpdateOrNull = config.contactToUpdateOrNull,
                        ),
                )
            }

            Configuration.Airplanes -> {
                val logger: ILogger by di.instance()
                val getAirplanes: GetAirplanes by di.instance()
                val deleteAirplane: DeleteAirplane by di.instance()

                Child.Airplanes(
                    component =
                        AirplanesComponent(
                            componentContext = context,
                            getAirplanes = getAirplanes,
                            logger = logger,
                            deleteAirplane = deleteAirplane,
                            onNavigateBack = {
                                navigation.pop()
                            },
                            onNavigateToAddAirplane = {
                                navigation.pushNew(Configuration.AddAirplane())
                            },
                            onNavigateToEditAirplane = {
                                navigation.pushNew(
                                    Configuration.AddAirplane(airplaneToUpdateOrNull = it),
                                )
                            },
                        ),
                )
            }

            is Configuration.AddAirplane -> {
                val logger: ILogger by di.instance()
                val addAirplane: AddAirplane by di.instance()
                val editAirplane: EditAirplane by di.instance()

                Child.AddAirplane(
                    component =
                        AddAirplaneComponent(
                            componentContext = context,
                            addAirplane = addAirplane,
                            editAirplane = editAirplane,
                            logger = logger,
                            onNavigateBack = {
                                navigation.pop()
                            },
                            onNavigateBackWithRefreshing = {
                                navigation.pop()
                                navigation.pop()
                                navigation.pushNew(Configuration.Airplanes)
                            },
                            airplaneToUpdateOrNull = config.airplaneToUpdateOrNull,
                        ),
                )
            }

            Configuration.AddLogbook -> {
                val logger: ILogger by di.instance()

                Child.AddLogbook(
                    component =
                        AddLogbookComponent(
                            componentContext = context,
                            logger = logger,
                            onNavigateBack = {
                                navigation.pop()
                            },
                        ),
                )
            }
        }
    }

    sealed class Child {
        data class Login(val component: LoginComponent) : Child()

        data class Flights(val component: FlightsComponent) : Child()

        data class Profile(val component: ProfileComponent) : Child()

        data class Carrier(val component: CarrierComponent) : Child()

        data class Onboarding(val component: OnboardingComponent) : Child()

        data class Contacts(val component: ContactsComponent) : Child()

        data class AddContact(val component: AddContactComponent) : Child()

        data class Airplanes(val component: AirplanesComponent) : Child()

        data class AddAirplane(val component: AddAirplaneComponent) : Child()

        data class AddLogbook(val component: AddLogbookComponent) : Child()
    }

    @Serializable
    sealed class Configuration {
        @Serializable
        data object Login : Configuration()

        @Serializable
        data object Flights : Configuration()

        @Serializable
        data object Profile : Configuration()

        @Serializable
        data object Carrier : Configuration()

        @Serializable
        data object Onboarding : Configuration()

        @Serializable
        data object Contacts : Configuration()

        @Serializable
        data class AddContact(val contactToUpdateOrNull: Contact? = null) : Configuration()

        @Serializable
        data object Airplanes : Configuration()

        @Serializable
        data class AddAirplane(val airplaneToUpdateOrNull: Airplane? = null) : Configuration()

        @Serializable
        data object AddLogbook : Configuration()
    }
}
