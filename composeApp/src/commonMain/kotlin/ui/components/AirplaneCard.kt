package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import domain.model.Airplane

interface AirplaneCard {
    data class MoreActions(
        val onEditAirplaneClick: () -> Unit,
        val onDeleteAirplaneClick: () -> Unit,
    )
}

@Composable
fun AirplaneCard(
    airplane: Airplane?,
    moreActions: AirplaneCard.MoreActions?,
    onAirplaneClick: () -> Unit,
    textIfAirplaneNull: String = "",
) {
    val shape = RoundedCornerShape(size = 8.dp)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(space = 16.dp),
        modifier =
            Modifier
                .shadow(
                    elevation = 4.dp,
                    spotColor = Color(0x40000000),
                    ambientColor = Color(0x40000000),
                    shape = shape,
                )
                .height(height = 80.dp)
                .background(color = Color.White, shape = shape)
                .clip(shape = shape)
                .clickable { onAirplaneClick() },
    ) {
        Photo(
            photoUrl = airplane?.imageUrl,
            modifier =
                Modifier.fillMaxHeight()
                    .width(width = 100.dp),
        )
        ModelAndRegistration(
            airplane = airplane,
            textIfAirplaneNull = textIfAirplaneNull,
            modifier = Modifier.weight(weight = 1f),
        )
        moreActions?.let {
            MoreIconWithDropdown { onDismiss ->
                DropdownMenuItem(
                    text = { Text("Edytuj samolot") },
                    onClick = {
                        moreActions.onEditAirplaneClick()
                        onDismiss()
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Edit,
                            contentDescription = null,
                        )
                    },
                )
                DropdownMenuItem(
                    text = { Text("Usuń samolot") },
                    onClick = {
                        moreActions.onDeleteAirplaneClick()
                        onDismiss()
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Delete,
                            contentDescription = null,
                        )
                    },
                )
            }
        }
    }
}

@Composable
private fun ModelAndRegistration(
    airplane: Airplane?,
    textIfAirplaneNull: String,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(space = 4.dp),
        modifier = modifier,
    ) {
        Text(
            text = airplane?.airplaneModel ?: textIfAirplaneNull,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        airplane?.let {
            Text(
                text = airplane.registrationNumber,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}
