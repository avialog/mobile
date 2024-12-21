package ui.utils

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

fun Modifier.conditional(
    isActive: Boolean,
    ifTrue: @Composable Modifier.() -> Modifier,
) = composed {
    if (isActive) {
        ifTrue()
    } else {
        this
    }
}

fun Modifier.clickableIfNotNull(onClick: (() -> Unit)?) =
    composed {
        if (onClick != null) {
            clickable(onClick = onClick)
        } else {
            this
        }
    }
