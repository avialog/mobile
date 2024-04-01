package ui.screens.home

data class HomeState(
    val token: String?,
)

sealed interface HomeEvent
