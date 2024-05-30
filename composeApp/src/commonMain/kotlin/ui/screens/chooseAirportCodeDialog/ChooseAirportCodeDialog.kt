package ui.screens.chooseAirportCodeDialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.PlaneDeparture

@Composable
internal fun ChooseAirportCodeDialog(
    show: Boolean,
    initialAirportCode: String,
    onAirportCodeSelected: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    if (show) {
        Dialog(onDismissRequest = onDismiss) {
            val codeState =
                remember {
                    mutableStateOf(initialAirportCode)
                }
            Card {
                Column(
                    verticalArrangement = Arrangement.spacedBy(space = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(all = 16.dp),
                ) {
                    Text("Podaj kod lotniska")
                    OutlinedTextField(
                        value = codeState.value,
                        onValueChange = {
                            codeState.value = it
                        },
                        singleLine = true,
                        label = {
                            Text(
                                text = "Kod lotniska",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.labelSmall,
                            )
                        },
                        keyboardOptions =
                            KeyboardOptions(
                                imeAction = ImeAction.Done,
                                capitalization = KeyboardCapitalization.Characters,
                            ),
                        leadingIcon = {
                            Icon(
                                imageVector = FontAwesomeIcons.Solid.PlaneDeparture,
                                contentDescription = null,
                                modifier = Modifier.size(size = 16.dp),
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                    )

                    Button(
                        onClick = {
                            onAirportCodeSelected(codeState.value)
                        },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(text = "Zatwierdź")
                    }
                    Button(
                        onClick = {
                            onDismiss()
                        },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(text = "Anuluj")
                    }
                }
            }
        }
    }
}
