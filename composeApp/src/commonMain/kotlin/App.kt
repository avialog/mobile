import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.decompose.router.stack.replaceCurrent
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.ChartBar
import compose.icons.fontawesomeicons.solid.Plane
import compose.icons.fontawesomeicons.solid.User
import navigation.RootComponent
import navigation.RootComponent.Configuration
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.screens.addContact.AddContactDestination
import ui.screens.airplanes.AirplanesDestination
import ui.screens.carrier.CarrierDestination
import ui.screens.contacts.ContactsDestination
import ui.screens.flights.FlightsDestination
import ui.screens.login.LoginDestination
import ui.screens.onboarding.OnboardingDestination
import ui.screens.profile.ProfileDestination
import kotlin.reflect.KClass

@Composable
@Preview
fun App(root: RootComponent) {
    MaterialTheme {
        val childStack by root.childStack.subscribeAsState()
        Column {
            Children(
                stack = childStack,
                animation = stackAnimation(slide()),
                modifier = Modifier.weight(weight = 1f),
            ) { child ->
                when (val instance = child.instance) {
                    is RootComponent.Child.Login -> LoginDestination(loginComponent = instance.component)
                    is RootComponent.Child.Flights -> FlightsDestination(flightsComponent = instance.component)
                    is RootComponent.Child.Carrier -> CarrierDestination(carrierComponent = instance.component)
                    is RootComponent.Child.Profile -> ProfileDestination(profileComponent = instance.component)
                    is RootComponent.Child.Onboarding -> OnboardingDestination(onboardingComponent = instance.component)
                    is RootComponent.Child.Contacts -> ContactsDestination(contactsComponent = instance.component)
                    is RootComponent.Child.AddContact -> AddContactDestination(addContactComponent = instance.component)
                    is RootComponent.Child.Airplanes -> AirplanesDestination(airplanesComponent = instance.component)
                }
            }
            AvialogBottomNavigation(
                currentConfiguration = {
                    childStack.active.configuration
                },
                onNavigateToFlights = {
                    root.navigation.replaceCurrent(configuration = Configuration.Flights)
                },
                onNavigateToCarrier = {
                    root.navigation.replaceCurrent(configuration = Configuration.Carrier)
                },
                onNavigateToProfile = {
                    root.navigation.replaceCurrent(configuration = Configuration.Profile)
                },
            )
        }
    }
}

private data class BottomNavigationItemData(
    val title: String,
    val icon: ImageVector,
    val configurationKClass: KClass<out Configuration>,
    val onClick: () -> Unit,
)

@Composable
private fun AvialogBottomNavigation(
    currentConfiguration: () -> Configuration,
    onNavigateToFlights: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToCarrier: () -> Unit,
) {
    val items =
        remember {
            listOf(
                BottomNavigationItemData(
                    title = "Flights",
                    icon = FontAwesomeIcons.Solid.Plane,
                    configurationKClass = Configuration.Flights::class,
                    onClick = onNavigateToFlights,
                ),
                BottomNavigationItemData(
                    title = "Carrier",
                    icon = FontAwesomeIcons.Solid.ChartBar,
                    configurationKClass = Configuration.Carrier::class,
                    onClick = onNavigateToCarrier,
                ),
                BottomNavigationItemData(
                    title = "Profile",
                    icon = FontAwesomeIcons.Solid.User,
                    configurationKClass = Configuration.Profile::class,
                    onClick = onNavigateToProfile,
                ),
            )
        }
    if (items.any {
            it.isSelected(currentConfiguration = currentConfiguration())
        }
    ) {
        NavigationBar(
            containerColor = Color(0xFFF3EDF7),
        ) {
            items.forEach {
                NavigationBarItem(
                    selected = it.isSelected(currentConfiguration = currentConfiguration()),
                    icon = {
                        Icon(
                            imageVector = it.icon,
                            contentDescription = null,
                            modifier = Modifier.size(size = 24.dp),
                        )
                    },
                    label = {
                        Text(text = it.title)
                    },
                    onClick = it.onClick,
                )
            }
        }
    }
}

private fun BottomNavigationItemData.isSelected(currentConfiguration: Configuration) =
    configurationKClass
        .isInstance(currentConfiguration)
