package ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InputWithSupportingText(
    input: @Composable () -> Unit,
    supportingText: String?,
    isError: Boolean,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(space = 4.dp),
    ) {
        input()
        supportingText?.let {
            Text(
                text = supportingText,
                color =
                    if (isError) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.onBackground
                    },
                fontSize = 12.sp,
            )
        }
    }
}
