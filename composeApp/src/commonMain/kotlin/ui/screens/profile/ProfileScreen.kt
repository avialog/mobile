package ui.screens.profile

import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun ProfileScreen(
    state: ProfileState,
    onNewEvent: (ProfileEvent) -> Unit,
) {
    Text(text = "Profile")
}
