package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ui.utils.conditional

@Composable
fun Loader(modifier: Modifier = Modifier) {
    CircularProgressIndicator(
        modifier = modifier.size(40.dp),
    )
}

@Composable
fun LoaderFullScreen(isOverlay: Boolean = false) {
    Box(
        contentAlignment = Alignment.Center,
        modifier =
            Modifier.fillMaxSize().conditional(isOverlay) {
                background(color = Color.Black.copy(alpha = 0.2f))
            },
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(40.dp),
        )
    }
}
