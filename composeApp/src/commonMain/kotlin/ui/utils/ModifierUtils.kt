package ui.utils

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
