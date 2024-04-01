package data.repository.auth

import dev.gitlive.firebase.auth.FirebaseAuth

class AuthRepository(
    private val firebaseAuth: FirebaseAuth,
) : IAuthRepository {
    override suspend fun getAuthToken(): String {
        return firebaseAuth.currentUser?.getIdToken(forceRefresh = false)
            ?: throw NoAuthTokenException()
    }

    override suspend fun loginWithEmailAndPassword(
        email: String,
        password: String,
    ) {
        firebaseAuth.signInWithEmailAndPassword(
            email = email,
            password = password,
        )
    }

    override suspend fun registerWithEmailAndPassword(
        email: String,
        password: String,
    ) {
        firebaseAuth.createUserWithEmailAndPassword(
            email = email,
            password = password,
        )
    }

    override suspend fun isUserLoggedIn(): Boolean = firebaseAuth.currentUser != null
}
