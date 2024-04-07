package domain.useCase

import data.repository.auth.IAuthRepository

class IsUserLoggedIn(
    private val authRepository: IAuthRepository,
) {
    suspend operator fun invoke() = authRepository.isUserLoggedIn()
}
