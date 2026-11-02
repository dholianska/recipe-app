package com.holianska.recipeapp.ui.home

import com.holianska.recipeapp.domain.profile.User

data class HomeState(
    val isLoading: Boolean = true,
    val user: User? = null,
    val loadFailed: Boolean = false,
)

sealed interface HomeIntent {
    data object Retry : HomeIntent
    data object SignOut : HomeIntent
    data object ResetOnboarding : HomeIntent
}

sealed interface HomeEffect {
    data object NavigateToAuth : HomeEffect
    data object NavigateToOnboarding : HomeEffect
}