package ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.TimeSource

data class InfoNotificationParams(
    val text: String,
    val duration: Duration? = null,
)

@Composable
fun InfoNotificationHandle(messagesFlow: Flow<InfoNotificationParams>) {
    val notificationParamsList =
        remember {
            mutableStateListOf<InfoNotificationParams>()
        }
    LaunchedEffect(messagesFlow) {
        messagesFlow.collect {
            notificationParamsList.add(it)
        }
    }

    Box {
        notificationParamsList.forEach {
            InfoNotification(params = it)
        }
    }
}

@Composable
private fun InfoNotification(params: InfoNotificationParams) {
    val isVisible =
        remember {
            mutableStateOf(false)
        }
    val progress =
        remember {
            mutableStateOf(0f)
        }
    LaunchedEffect(Unit) {
        isVisible.value = true
        if (params.duration != null) {
            val timeSource = TimeSource.Monotonic
            val mark = timeSource.markNow()
            while (true) {
                delay(10.milliseconds)
                val now = mark.elapsedNow()
                if (now >= params.duration) {
                    progress.value = 1.0f
                    isVisible.value = false
                    break
                } else {
                    progress.value = (now / params.duration).toFloat()
                }
            }
        }
    }
    AnimatedVisibility(
        visible = isVisible.value,
        exit = shrinkVertically() + fadeOut(),
    ) {
        Column(
            modifier =
                Modifier.background(
                    color = Color(0xFFD65745),
                ),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(space = 4.dp),
                modifier = Modifier.padding(start = 12.dp),
            ) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = null,
                    tint = Color.White,
                )
                Text(
                    text = params.text,
                    color = Color.White,
                    modifier = Modifier.weight(weight = 1f),
                )
                IconButton(
                    onClick = {
                        isVisible.value = false
                    },
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = null,
                        tint = Color.White,
                    )
                }
            }
            ProgressBox(progress = progress::value)
        }
    }
}

@Composable
private fun ProgressBox(progress: () -> Float) {
    Box(
        modifier =
            Modifier
                .layout { measurable, constraints ->
                    val progressWith = (progress() * constraints.maxWidth).toInt()
                    val placeable =
                        measurable.measure(
                            constraints = Constraints.fixedWidth(width = progressWith),
                        )
                    layout(width = placeable.width, height = placeable.height) {
                        placeable.place(x = 0, y = 0)
                    }
                }.height(height = 5.dp)
                .background(color = Color(0xFFF3CDC8)),
    )
}
