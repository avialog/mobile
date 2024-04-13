package ui.screens.carrier

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun CarrierScreen(
    state: CarrierState,
    onNewEvent: (CarrierEvent) -> Unit,
) {
    Text(text = "Carrier")
}
