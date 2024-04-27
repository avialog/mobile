package ui.screens.addContact

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Briefcase
import compose.icons.fontawesomeicons.solid.StickyNote
import ui.components.InputWithSupportingText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddContactScreen(
    state: AddContactState,
    onNewEvent: (AddContactEvent) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = state.getScreenText(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onNewEvent(AddContactEvent.BackClick)
                        },
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .padding(paddingValues = innerPadding)
                    .consumeWindowInsets(innerPadding),
        ) {
            Content(
                state = state,
                onNewEvent = onNewEvent,
            )
        }
    }
}

@Composable
private fun Content(
    state: AddContactState,
    onNewEvent: (AddContactEvent) -> Unit,
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
    ) {
        InputFields(
            state = state,
            onNewEvent = onNewEvent,
            modifier = Modifier.weight(weight = 1f),
        )
        Button(
            onClick = {
                onNewEvent(AddContactEvent.SaveClick)
            },
            shape = RoundedCornerShape(size = 4.dp),
            enabled = !state.isRequestInProgress,
            modifier =
                Modifier.padding(vertical = 16.dp)
                    .heightIn(min = 54.dp).fillMaxWidth(),
        ) {
            Text(text = state.getScreenText())
        }
    }
}

private fun AddContactState.getScreenText() =
    when (idToUpdateOrNull) {
        null -> "Dodaj kontakt"
        else -> "Edytuj kontakt"
    }

@Composable
private fun InputFields(
    state: AddContactState,
    onNewEvent: (AddContactEvent) -> Unit,
    modifier: Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(space = 24.dp),
        modifier = modifier.verticalScroll(state = rememberScrollState()),
    ) {
        val focusManager = LocalFocusManager.current

        val showNameError = state.name.isBlank() && state.showErrorIfAny
        InputWithSupportingText(
            input = {
                OutlinedTextField(
                    value = state.name,
                    onValueChange = {
                        onNewEvent(AddContactEvent.NameChange(it))
                    },
                    singleLine = true,
                    label = {
                        Text(text = "Imię*")
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions =
                        KeyboardActions(
                            onNext = {
                                focusManager.moveFocus(FocusDirection.Down)
                            },
                        ),
                    modifier = Modifier.fillMaxWidth(),
                )
            },
            isError = showNameError,
            supportingText = "Imię jest wymagane!".takeIf { showNameError },
        )

        OutlinedTextField(
            value = state.surname,
            onValueChange = {
                onNewEvent(AddContactEvent.SurnameChange(it))
            },
            singleLine = true,
            label = {
                Text(text = "Nazwisko")
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions =
                KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    },
                ),
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = state.phoneNumber,
            onValueChange = {
                onNewEvent(AddContactEvent.PhoneNumberChange(it))
            },
            singleLine = true,
            label = {
                Text(text = "Numer telefonu")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Phone,
                    contentDescription = null,
                )
            },
            keyboardOptions =
                KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next,
                ),
            keyboardActions =
                KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    },
                ),
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = state.email,
            onValueChange = {
                onNewEvent(AddContactEvent.EmailChange(it))
            },
            singleLine = true,
            label = {
                Text(text = "E-mail")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Email,
                    contentDescription = null,
                )
            },
            keyboardOptions =
                KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                ),
            keyboardActions =
                KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    },
                ),
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = state.companyName,
            onValueChange = {
                onNewEvent(AddContactEvent.CompanyNameChange(it))
            },
            singleLine = true,
            label = {
                Text(text = "Firma")
            },
            leadingIcon = {
                Icon(
                    imageVector = FontAwesomeIcons.Solid.Briefcase,
                    contentDescription = null,
                    modifier = Modifier.size(size = 24.dp),
                )
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions =
                KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    },
                ),
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = state.notes,
            onValueChange = {
                onNewEvent(AddContactEvent.NotesChange(it))
            },
            label = {
                Text(text = "Notatki")
            },
            leadingIcon = {
                Icon(
                    imageVector = FontAwesomeIcons.Solid.StickyNote,
                    contentDescription = null,
                    modifier = Modifier.size(size = 24.dp),
                )
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            modifier =
                Modifier.fillMaxWidth()
                    .heightIn(min = 140.dp),
        )
    }
}
