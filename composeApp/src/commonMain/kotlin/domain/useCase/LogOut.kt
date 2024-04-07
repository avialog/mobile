package domain.useCase

import data.repository.auth.IAuthRepository

class LogOut(
    private val authRepository: IAuthRepository,
) {
    suspend operator fun invoke() {
        authRepository.logOut()
    }
}
