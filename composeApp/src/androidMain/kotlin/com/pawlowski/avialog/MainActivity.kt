package com.pawlowski.avialog

import App
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.retainedComponent
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.initialize
import navigation.RootComponent

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalDecomposeApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Firebase.initialize(this)

        val root =
            retainedComponent {
                RootComponent(it)
            }
        setContent {
            App(root = root)
        }
    }
}
