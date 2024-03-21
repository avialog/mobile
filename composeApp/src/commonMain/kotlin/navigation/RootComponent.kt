package navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import kotlinx.serialization.Serializable
import screens.home.HomeComponent
import screens.login.LoginComponent

class RootComponent(
    componentContext: ComponentContext,
) : ComponentContext by componentContext {
    private val navigation = StackNavigation<Configuration>()
    val childStack =
        childStack(
            source = navigation,
            serializer = Configuration.serializer(),
            initialConfiguration = Configuration.Login,
            handleBackButton = true,
            childFactory = ::createChild,
        )

    @OptIn(ExperimentalDecomposeApi::class)
    private fun createChild(
        config: Configuration,
        context: ComponentContext,
    ): Child {
        return when (config) {
            Configuration.Login ->
                Child.Login(
                    LoginComponent(
                        componentContext = context,
                        onNavigateToHome = {
                            navigation.pushNew(Configuration.Home)
                        },
                    ),
                )

            Configuration.Home -> {
                Child.Home(
                    component =
                        HomeComponent(
                            componentContext = context,
                            onNavigateToLogin = {
                                navigation.pop()
                            },
                        ),
                )
            }
        }
    }

    sealed class Child {
        data class Login(val component: LoginComponent) : Child()

        data class Home(val component: HomeComponent) : Child()
    }

    @Serializable
    sealed class Configuration {
        @Serializable
        data object Login : Configuration()

        @Serializable
        data object Home : Configuration()
    }
}
