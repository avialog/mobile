package data.repository.auth

interface IAuthRepository {
    suspend fun getAuthToken(): String

    suspend fun loginWithEmailAndPassword(
        email: String,
        password: String,
    )

    suspend fun registerWithEmailAndPassword(
        email: String,
        password: String,
    )

    suspend fun isUserLoggedIn(): Boolean
}
