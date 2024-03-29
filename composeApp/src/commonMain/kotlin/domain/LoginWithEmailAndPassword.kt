package domain

import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

class LoginWithEmailAndPassword {
    suspend operator fun invoke(
        email: String,
        password: String,
    ) {
        delay(2.seconds)
    }
}
