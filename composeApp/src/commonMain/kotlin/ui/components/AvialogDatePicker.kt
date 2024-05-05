package ui.components

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import ui.utils.toEpochMillisecondsAsUtc

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvialogDatePicker(
    onDismiss: () -> Unit,
    onConfirm: (LocalDate?) -> Unit,
    initial: LocalDate? = null,
) {
    val datePickerState =
        rememberDatePickerState(
            initialSelectedDateMillis =
                initial?.toEpochMillisecondsAsUtc(),
        )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    val result =
                        datePickerState.selectedDateMillis?.let {
                            Instant.fromEpochMilliseconds(it).toLocalDateTime(timeZone = TimeZone.UTC).date
                        }
                    onConfirm(result)
                },
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                },
            ) {
                Text("Cancel")
            }
        },
    ) {
        DatePicker(state = datePickerState)
    }
}
