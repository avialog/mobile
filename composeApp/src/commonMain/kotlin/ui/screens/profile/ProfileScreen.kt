package ui.screens.profile

import Resource
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.AddressBook
import compose.icons.fontawesomeicons.solid.AngleRight
import compose.icons.fontawesomeicons.solid.Lock
import compose.icons.fontawesomeicons.solid.Plane
import compose.icons.fontawesomeicons.solid.User
import domain.model.Profile
import ui.components.Loader

@Composable
fun ProfileScreen(
    state: ProfileState,
    onNewEvent: (ProfileEvent) -> Unit,
) {
    Column(
        modifier =
            Modifier
                .verticalScroll(state = rememberScrollState())
                .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ProfileCard(
            profileResource = state.profileResource,
            onNewEvent = onNewEvent,
        )
        UserDataSection(onNewEvent = onNewEvent)
        SettingsSection(onNewEvent = onNewEvent)

        TextButton(
            onClick = {
                onNewEvent(ProfileEvent.LogOutClick)
            },
            enabled = !state.isLogOutInProgress,
        ) {
            Text(text = "Wyloguj się")
        }
    }
}

@Composable
private fun ProfileCard(
    profileResource: Resource<Profile>,
    onNewEvent: (ProfileEvent) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(space = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier =
            Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = Color(0xFFC0C9C0),
                    shape = RoundedCornerShape(size = 8.dp),
                )
                .padding(all = 24.dp),
    ) {
        when (profileResource) {
            is Resource.Error -> {
                Column(
                    verticalArrangement = Arrangement.spacedBy(space = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = "Error loading profile data",
                        textAlign = TextAlign.Center,
                    )
                    Button(
                        onClick = {
                            onNewEvent(ProfileEvent.RetryClick)
                        },
                    ) {
                        Text(text = "Try again")
                    }
                }
            }
            Resource.Loading -> {
                Loader()
            }
            is Resource.Success -> {
                val profile = profileResource.data
                NameAndEmail(
                    firstName = profile.firstName,
                    lastName = profile.lastName,
                    email = profile.email,
                    modifier = Modifier.weight(weight = 1f),
                )
                Avatar(profile = profile)
            }
        }
    }
}

@Composable
private fun NameAndEmail(
    firstName: String?,
    lastName: String?,
    email: String,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
        modifier = modifier,
    ) {
        if (firstName != null || lastName != null) {
            Text(
                text = "${firstName ?: ""} ${lastName ?: ""}".trim(),
                style =
                    TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight(500),
                        color = Color(0xFF201A19),
                    ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Text(
            text = email,
            style =
                TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight(500),
                    color = Color(0xFF201A19),
                ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun Avatar(profile: Profile) {
    Box(
        modifier =
            Modifier
                .size(size = 56.dp)
                .background(
                    color = Color(0xFF92F7BC),
                    shape = CircleShape,
                ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = profile.getAvatarLetters(),
            style =
                TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight(500),
                    color = Color(0xFF002111),
                    textAlign = TextAlign.Center,
                ),
        )
    }
}

private fun Profile.getAvatarLetters(): String =
    when {
        !firstName.isNullOrEmpty() && !lastName.isNullOrEmpty() -> {
            "${firstName.first()}${lastName.first()}"
        }
        !firstName.isNullOrEmpty() -> firstName.take(n = 2)
        !lastName.isNullOrEmpty() -> lastName.take(n = 2)
        else -> email.take(n = 2)
    }.uppercase()

@Composable
private fun UserDataSection(onNewEvent: (ProfileEvent) -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(space = 4.dp),
    ) {
        SectionHeader(title = "Dane użytkownika")
        SectionItem(
            icon = FontAwesomeIcons.Solid.Plane,
            title = "Samoloty",
            onClick = {
                onNewEvent(ProfileEvent.PlanesClick)
            },
        )
        HorizontalDivider()
        SectionItem(
            icon = FontAwesomeIcons.Solid.AddressBook,
            title = "Kontakty",
            onClick = {
                onNewEvent(ProfileEvent.ContactsClick)
            },
        )
    }
}

@Composable
private fun SettingsSection(onNewEvent: (ProfileEvent) -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(space = 4.dp),
    ) {
        SectionHeader(title = "Ustawienia")
        SectionItem(
            icon = FontAwesomeIcons.Solid.User,
            title = "Dane osobowe",
            onClick = {
                onNewEvent(ProfileEvent.PlanesClick)
            },
        )
        HorizontalDivider()
        SectionItem(
            icon = FontAwesomeIcons.Solid.Lock,
            title = "Hasło",
            onClick = {
                onNewEvent(ProfileEvent.PasswordClick)
            },
        )
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style =
            TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight(500),
                color = Color(0xFF191C1A),
            ),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
private fun SectionItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(space = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier =
            Modifier
                .clickable { onClick() }
                .padding(vertical = 16.dp),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(size = 24.dp),
        )
        Text(
            text = title,
            style =
                TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight(400),
                    color = Color(0xFF191C1A),
                ),
            modifier = Modifier.weight(weight = 1f),
        )
        Icon(
            imageVector = FontAwesomeIcons.Solid.AngleRight,
            contentDescription = null,
            modifier = Modifier.size(size = 24.dp),
        )
    }
}
