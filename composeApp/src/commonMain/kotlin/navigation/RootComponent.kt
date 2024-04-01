package navigation

import ILogger
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.replaceCurrent
import data.repository.auth.IAuthRepository
import di.di
import domain.IsUserLoggedIn
import domain.LoginWithEmailAndPassword
import domain.RegisterWithEmailAndPassword
import kotlinx.serialization.Serializable
import org.kodein.di.instance
import ui.screens.carrier.CarrierComponent
import ui.screens.flights.FlightsComponent
import ui.screens.login.AreLoginInputsValid
import ui.screens.login.LoginComponent
import ui.screens.profile.ProfileComponent

class RootComponent(
    componentContext: ComponentContext,
) : ComponentContext by componentContext {
    val navigation = StackNavigation<Configuration>()
    val childStack =
        childStack(
            source = navigation,
            serializer = Configuration.serializer(),
            initialConfiguration = Configuration.Login,
            handleBackButton = true,
            childFactory = ::createChild,
        )

    private fun createChild(
        config: Configuration,
        context: ComponentContext,
    ): Child {
        return when (config) {
            Configuration.Login -> {
                val loginWithEmailAndPassword: LoginWithEmailAndPassword by di.instance()
                val registerWithEmailAndPassword: RegisterWithEmailAndPassword by di.instance()
                val isUserLoggerIn: IsUserLoggedIn by di.instance()
                val areLoginInputsValid: AreLoginInputsValid by di.instance()
                val logger: ILogger by di.instance()

                Child.Login(
                    LoginComponent(
                        componentContext = context,
                        onNavigateToHome = {
                            navigation.replaceCurrent(Configuration.Flights)
                        },
                        loginWithEmailAndPassword = loginWithEmailAndPassword,
                        registerWithEmailAndPassword = registerWithEmailAndPassword,
                        isUserLoggedIn = isUserLoggerIn,
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
                Child.Profile(
                    component =
                        ProfileComponent(
                            componentContext = context,
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
    }
}
