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
import domain.model.Role
import ui.components.ActionListItem
import ui.components.BaseBottomSheet
import ui.components.RoleBox

@Composable
internal fun ChooseRoleBottomSheet(
    show: Boolean,
    roles: List<Role>,
    selectedRole: Role?,
    onChooseRole: (Role) -> Unit,
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
                text = "Wybierz rolę",
                style = MaterialTheme.typography.titleMedium,
            )
            roles.forEachIndexed { index, role ->
                ActionListItem(
                    title = role.name,
                    leading = {
                        RoleBox(role = role)
                    },
                    trailing = {
                        RadioButton(
                            selected = role == selectedRole,
                            onClick = {
                                onChooseRole(role)
                            },
                        )
                    },
                    onClick = {
                        onChooseRole(role)
                    },
                )
                if (index != roles.lastIndex) {
                    HorizontalDivider()
                }
            }
        }
    }
}
