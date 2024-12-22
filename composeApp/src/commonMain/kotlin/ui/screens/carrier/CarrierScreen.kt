package ui.screens.carrier

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import ui.components.ErrorItem
import ui.components.LoaderFullScreen

@Composable
fun CarrierScreen(
    state: CarrierState,
    onNewEvent: (CarrierEvent) -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        when (state.reportResult) {
            is Resource.Error -> ErrorItem(onRetryClick = { onNewEvent(CarrierEvent.RetryClick) })
            Resource.Loading -> LoaderFullScreen()
            is Resource.Success ->
                Text(
                    text = "Raport został wygenerowany i zapisany w wybranej lokalizacji",
                    textAlign = TextAlign.Center,
                )
            null -> {
                Button(
                    onClick = {
                        onNewEvent(CarrierEvent.GenerateRaport)
                    },
                ) {
                    Text(text = "Generuj raport")
                }
            }
        }
    }
}
