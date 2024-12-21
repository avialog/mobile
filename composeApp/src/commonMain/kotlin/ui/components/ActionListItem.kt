package ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.utils.clickableIfNotNull

@Composable
fun ActionListItem(
    title: String,
    subtitle: String? = null,
    leading: @Composable () -> Unit = {},
    modifier: Modifier = Modifier,
    trailing: @Composable () -> Unit = {},
    onClick: (() -> Unit)? = null,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(space = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier =
            modifier.clickableIfNotNull(onClick),
    ) {
        leading()
        Column(
            verticalArrangement = Arrangement.spacedBy(space = 4.dp),
            modifier = Modifier.weight(weight = 1f),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
            )
            subtitle?.let {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
        trailing()
    }
}
