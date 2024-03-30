package ui.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Regular
import compose.icons.fontawesomeicons.regular.Eye
import compose.icons.fontawesomeicons.regular.EyeSlash
import ui.components.Loader
import ui.components.LoaderFullScreen

@Composable
fun LoginScreen(
    state: LoginState,
    onNewEvent: (LoginEvent) -> Unit,
) {
    if (state.showFullScreenLoader) {
        LoaderFullScreen()
    } else {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(space = 24.dp),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 30.dp),
        ) {
            Headers()
            Inputs(
                email = state.email,
                password = state.password,
                onNewEvent = onNewEvent,
            )
            TextButton(
                onClick = {
                    onNewEvent(LoginEvent.ForgotPasswordClick)
                },
                modifier = Modifier.align(Alignment.End),
            ) {
                Text(
                    text = "Forgot password",
                    style =
                        TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight(600),
                            color = Color(0xFFFB344F),
                        ),
                )
            }
            Buttons(
                isRequestInProgress = state.isRequestInProgress,
                onNewEvent = onNewEvent,
            )
        }
    }
}

@Composable
private fun Inputs(
    email: String,
    password: String,
    onNewEvent: (LoginEvent) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(space = 16.dp),
    ) {
        val focusManager = LocalFocusManager.current
        OutlinedTextField(
            value = email,
            label = {
                Text(text = "E-mail")
            },
            onValueChange = {
                onNewEvent(LoginEvent.EmailChange(it))
            },
            singleLine = true,
            keyboardOptions =
                KeyboardOptions(
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
        val showPassword =
            remember {
                mutableStateOf(false)
            }
        OutlinedTextField(
            value = password,
            label = {
                Text(text = "Password")
            },
            onValueChange = {
                onNewEvent(LoginEvent.PasswordChange(it))
            },
            singleLine = true,
            keyboardOptions =
                KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                ),
            trailingIcon = {
                val icon =
                    if (showPassword.value) {
                        FontAwesomeIcons.Regular.EyeSlash
                    } else {
                        FontAwesomeIcons.Regular.Eye
                    }

                IconButton(onClick = { showPassword.value = !showPassword.value }, modifier = Modifier.size(size = 24.dp)) {
                    Icon(
                        imageVector = icon,
                        contentDescription = "Visibility",
                        tint = Color.Black,
                    )
                }
            },
            visualTransformation = if (showPassword.value) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun Headers(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = "Hi, sign in to Avialog! \uD83D\uDC4B",
            fontWeight = FontWeight.SemiBold,
            fontSize = 25.sp,
        )
        Text(
            text = "Put your data and let’s start!",
            style =
                TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight(600),
                    color = Color(0xFF999EA1),
                ),
        )
    }
}

@Composable
private fun Buttons(
    isRequestInProgress: Boolean,
    onNewEvent: (LoginEvent) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
    ) {
        Button(
            onClick = {
                onNewEvent(LoginEvent.LoginClick)
            },
            enabled = !isRequestInProgress,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Log in")
        }
        OutlinedButton(
            onClick = {
                onNewEvent(LoginEvent.RegisterClick)
            },
            enabled = !isRequestInProgress,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Register")
        }
        if (isRequestInProgress) {
            Loader(modifier = Modifier.align(Alignment.CenterHorizontally))
        }
    }
}
