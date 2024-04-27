package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import coil3.compose.SubcomposeAsyncImage

@Composable
fun Photo(
    photoUrl: String?,
    modifier: Modifier = Modifier,
) {
    SubcomposeAsyncImage(
        model = photoUrl,
        contentDescription = null,
        contentScale = ContentScale.FillBounds,
        loading = {
            Box(modifier = Modifier.fillMaxSize().background(color = Color.LightGray))
        },
        error = {
            Box(modifier = Modifier.fillMaxSize().background(color = Color.LightGray))
        },
        modifier = modifier,
    )
}
