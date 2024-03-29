package di

import data.repository.auth.AuthRepository
import data.repository.auth.IAuthRepository
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.auth
import domain.IsUserLoggedIn
import domain.LoginWithEmailAndPassword
import domain.RegisterWithEmailAndPassword
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.bindSingleton
import org.kodein.di.instance

val di =
    DI {
        bindProvider<LoginWithEmailAndPassword> {
            LoginWithEmailAndPassword(
                authRepository = instance(),
            )
        }
        bindProvider<RegisterWithEmailAndPassword> {
            RegisterWithEmailAndPassword(
                authRepository = instance(),
            )
        }
        bindProvider<IsUserLoggedIn> {
            IsUserLoggedIn(
                authRepository = instance(),
            )
        }
        bindProvider<IAuthRepository> {
            AuthRepository(
                firebaseAuth = instance(),
            )
        }
        bindSingleton<FirebaseAuth> {
            Firebase.auth
        }
    }
