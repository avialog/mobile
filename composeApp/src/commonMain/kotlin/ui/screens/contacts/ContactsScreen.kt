package ui.screens.contacts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import domain.model.Contact
import ui.components.ErrorItem
import ui.components.LoaderFullScreen

@Composable
fun ContactsScreen(
    state: ContactsState,
    onNewEvent: (ContactsEvent) -> Unit,
) {
    Column {
        TopAppBar(
            title = {
                Text(text = "Kontakty")
            },
            navigationIcon = {
                IconButton(
                    onClick = {
                        onNewEvent(ContactsEvent.BackClick)
                    },
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                    )
                }
            },
            backgroundColor = Color.White,
            contentColor = Color.Black,
        )

        when (state.contactsResource) {
            is Resource.Error -> {
                ErrorItem(
                    onRetryClick = {
                        onNewEvent(ContactsEvent.RetryClick)
                    },
                )
            }
            Resource.Loading -> {
                LoaderFullScreen()
            }
            is Resource.Success ->
                Content(
                    contacts = state.contactsResource.data,
                    onNewEvent = onNewEvent,
                )
        }
    }
}

@Composable
private fun Content(
    contacts: List<Contact>,
    onNewEvent: (ContactsEvent) -> Unit,
) {
    Column {
        val groupedContacts =
            remember(contacts) {
                contacts.groupBy { it.firstName.first() }
            }

        groupedContacts.entries.forEach { (firstCharacter, contactList) ->
            CharacterSectionHeader(character = firstCharacter)

            contactList.forEach {
                ContactItem(
                    contact = it,
                    onClick = {
                        onNewEvent(ContactsEvent.ContactClick(it))
                    },
                )
            }
        }
    }
}

@Composable
private fun CharacterSectionHeader(character: Char) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
    ) {
        Text(
            text = character.toString(),
            style =
                TextStyle(
                    fontSize = 22.sp,
                    fontWeight = FontWeight(400),
                    color = Color(0xFF000000),
                ),
        )
    }
}

@Composable
private fun ContactItem(
    contact: Contact,
    onClick: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(space = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier =
            Modifier.fillMaxWidth()
                .padding(all = 16.dp),
    ) {
        Avatar(photoUrl = contact.avatarUrl)
        NameAndSurnameColumn(
            contact = contact,
            modifier = Modifier.weight(weight = 1f),
        )
        MoreIconWithDropdown()
    }
}

@Composable
private fun NameAndSurnameColumn(
    contact: Contact,
    modifier: Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(space = 4.dp),
        modifier = modifier,
    ) {
        Text(
            text = "${contact.firstName} ${contact.lastName ?: ""}",
            style =
                TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight(500),
                    color = Color(0xFF1D1B20),
                ),
        )
        contact.phone?.let {
            Text(
                text = contact.phone,
                style =
                    TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight(400),
                        color = Color(0xFF1D1B20),
                    ),
            )
        }
    }
}

@Composable
private fun Avatar(photoUrl: String?) {
    Box(
        modifier =
            Modifier.size(size = 40.dp)
                .background(color = Color.LightGray, shape = CircleShape),
    )
}

@Composable
private fun MoreIconWithDropdown() {
    IconButton(onClick = { }) {
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = null,
        )
    }
}
