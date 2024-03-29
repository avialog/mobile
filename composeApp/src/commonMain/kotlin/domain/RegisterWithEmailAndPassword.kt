package domain

import data.repository.auth.IAuthRepository

class RegisterWithEmailAndPassword(
    private val authRepository: IAuthRepository,
) {
    suspend operator fun invoke(
        email: String,
        password: String,
    ) {
        authRepository.registerWithEmailAndPassword(
            email = email,
            password = password,
        )
    }
}
