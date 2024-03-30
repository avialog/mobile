package ui.screens.login

class AreLoginInputsValid {
    operator fun invoke(
        email: String,
        password: String,
    ): Boolean =
        email.getEmailErrorTextOrNull() == null &&
            password.getPasswordErrorTextOrNull() == null
}

fun String.getEmailErrorTextOrNull(): String? =
    when {
        isEmpty() -> "E-mail can not be empty!"
        else -> null
    }

fun String.getPasswordErrorTextOrNull(): String? =
    when {
        length >= 6 -> null
        else -> "Password must be minimum 6 characters length!"
    }
