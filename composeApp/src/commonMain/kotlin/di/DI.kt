package di

import ILogger
import LoggerDebug
import data.network.AvialogDataProvider
import data.network.GetHttpClient
import data.repository.auth.AuthRepository
import data.repository.auth.IAuthRepository
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.auth
import domain.useCase.GetContacts
import domain.useCase.GetProfile
import domain.useCase.IsUserLoggedIn
import domain.useCase.LogOut
import domain.useCase.LoginWithEmailAndPassword
import domain.useCase.RegisterWithEmailAndPassword
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import ui.screens.login.AreLoginInputsValid

@OptIn(ExperimentalSerializationApi::class)
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
        bindProvider<AreLoginInputsValid> {
            AreLoginInputsValid()
        }
        bindProvider<ILogger> {
            LoggerDebug()
        }
        bindSingleton<GetHttpClient> {
            GetHttpClient(
                json = instance(),
            )
        }
        bindProvider {
            AvialogDataProvider(
                getHttpClient = instance(),
                authRepository = instance(),
            )
        }
        bindProvider {
            GetProfile(
                avialogDataProvider = instance(),
            )
        }
        bindProvider<Json> {
            Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
                explicitNulls = false
            }
        }
        bindProvider<LogOut> {
            LogOut(
                authRepository = instance(),
            )
        }
        bindProvider<GetContacts> {
            GetContacts(
                avialogDataProvider = instance(),
            )
        }
    }
