package navigation

import ILogger
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.navigate
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.router.stack.replaceCurrent
import data.repository.auth.IAuthRepository
import di.di
import domain.useCase.GetProfile
import domain.useCase.IsUserLoggedIn
import domain.useCase.LoginWithEmailAndPassword
import domain.useCase.RegisterWithEmailAndPassword
import kotlinx.serialization.Serializable
import org.kodein.di.instance
import ui.screens.carrier.CarrierComponent
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
                Child.Profile(
                    component =
                        ProfileComponent(
                            componentContext = context,
                            logger = logger,
                            getProfile = getProfile,
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
        }
    }

    sealed class Child {
        data class Login(val component: LoginComponent) : Child()

        data class Flights(val component: FlightsComponent) : Child()

        data class Profile(val component: ProfileComponent) : Child()

        data class Carrier(val component: CarrierComponent) : Child()

        data class Onboarding(val component: OnboardingComponent) : Child()
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
    }
}
