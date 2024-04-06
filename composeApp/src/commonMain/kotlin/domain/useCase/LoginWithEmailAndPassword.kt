package domain.useCase

import data.repository.auth.IAuthRepository

class LoginWithEmailAndPassword(
    private val authRepository: IAuthRepository,
) {
    suspend operator fun invoke(
        email: String,
        password: String,
    ) {
        authRepository.loginWithEmailAndPassword(
            email = email,
            password = password,
        )
    }
}
