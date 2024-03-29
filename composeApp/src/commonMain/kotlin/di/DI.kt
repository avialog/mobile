package di

import domain.LoginWithEmailAndPassword
import org.kodein.di.DI
import org.kodein.di.bindProvider

val di =
    DI {
        bindProvider<LoginWithEmailAndPassword> { LoginWithEmailAndPassword() }
    }
