package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import domain.model.Role
import domain.model.toBoxColor

@Composable
fun RoleBox(role: Role) {
    Box(
        modifier =
            Modifier
                .size(size = 31.dp)
                .background(
                    color = role.toBoxColor(),
                    shape = RoundedCornerShape(size = 4.dp),
                ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = role.name,
            color = Color.White,
            style = MaterialTheme.typography.titleSmall,
        )
    }
}
