package ui.screens.contacts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import domain.model.Contact
import ui.components.ErrorItem
import ui.components.LoaderFullScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactsScreen(
    state: ContactsState,
    onNewEvent: (ContactsEvent) -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Kontakty",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleLarge,
                    )
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
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onNewEvent(ContactsEvent.AddContactClick)
                },
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                )
            }
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier.padding(paddingValues = innerPadding)
                    .consumeWindowInsets(innerPadding),
        ) {
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
                    onNewEvent = onNewEvent,
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
            style = MaterialTheme.typography.titleLarge,
        )
    }
}

@Composable
private fun ContactItem(
    contact: Contact,
    onNewEvent: (ContactsEvent) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(space = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier =
            Modifier.fillMaxWidth()
                .clickable {
                    onNewEvent(ContactsEvent.ContactClick(contact = contact))
                }
                .padding(all = 16.dp),
    ) {
        Avatar(photoUrl = contact.avatarUrl)
        NameAndSurnameColumn(
            contact = contact,
            modifier = Modifier.weight(weight = 1f),
        )
        MoreIconWithDropdown(
            contact = contact,
            onNewEvent = onNewEvent,
        )
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
            style = MaterialTheme.typography.titleMedium,
        )
        contact.phone?.let {
            Text(
                text = contact.phone,
                style = MaterialTheme.typography.bodyMedium,
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
private fun MoreIconWithDropdown(
    contact: Contact,
    onNewEvent: (ContactsEvent) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(Icons.Default.MoreVert, contentDescription = null)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            DropdownMenuItem(
                text = { Text("Edytuj kontakt") },
                onClick = { onNewEvent(ContactsEvent.EditContactClick(contact = contact)) },
                leadingIcon = {
                    Icon(
                        Icons.Outlined.Edit,
                        contentDescription = null,
                    )
                },
            )
            DropdownMenuItem(
                text = { Text("Usuń kontakt") },
                onClick = { onNewEvent(ContactsEvent.DeleteContactClick(contact = contact)) },
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
