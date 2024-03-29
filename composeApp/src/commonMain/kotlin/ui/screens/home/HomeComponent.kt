package ui.screens.home

import com.arkivanov.decompose.ComponentContext

class HomeComponent(
    componentContext: ComponentContext,
    private val onNavigateToLogin: () -> Unit,
) : ComponentContext by componentContext
