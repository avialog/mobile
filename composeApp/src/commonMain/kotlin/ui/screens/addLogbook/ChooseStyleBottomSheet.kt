package ui.screens.addLogbook

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import domain.model.Style
import ui.components.ActionListItem
import ui.components.BaseBottomSheet

@Composable
internal fun ChooseStyleBottomSheet(
    show: Boolean,
    styles: List<Style>,
    selectedStyle: Style?,
    onChooseStyle: (Style) -> Unit,
    onDismiss: () -> Unit,
) {
    BaseBottomSheet(show = show, onDismiss = onDismiss) {
        Column(
            verticalArrangement = Arrangement.spacedBy(space = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(bottom = 15.dp)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(state = rememberScrollState()),
        ) {
            Text(
                text = "Wybierz styl",
                style = MaterialTheme.typography.titleMedium,
            )
            styles.forEachIndexed { index, style ->
                ActionListItem(
                    title = style.name,
                    trailing = {
                        RadioButton(
                            selected = style == selectedStyle,
                            onClick = {
                                onChooseStyle(style)
                            },
                        )
                    },
                    onClick = {
                        onChooseStyle(style)
                    },
                )
                if (index != styles.lastIndex) {
                    HorizontalDivider()
                }
            }
        }
    }
}
