package ui.screens.flights

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp

@Composable
fun FlightsScreen(
    state: FlightsState,
    onNewEvent: (FlightsEvent) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(space = 16.dp),
        modifier = Modifier.verticalScroll(state = rememberScrollState()),
    ) {
        Text(text = "Home")
        Text(text = state.token ?: "Loading token...")
        val clipboardManager = LocalClipboardManager.current
        Button(onClick = {
            state.token?.let {
                clipboardManager.setText(
                    buildAnnotatedString {
                        append(it)
                    },
                )
            }
        }) {
            Text(text = "Copy token")
        }
    }
}
