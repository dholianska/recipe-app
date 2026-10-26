package com.holianska.recipeapp.ui.main

import androidx.lifecycle.ViewModel
import com.holianska.recipeapp.domain.usecase.IsOnboardingCompletedUseCase
import com.holianska.recipeapp.navigation.Route
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class MainState(val startRoute: Route? = null)

class MainViewModel(
    isOnboardingCompleted: IsOnboardingCompletedUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(
        MainState(
            startRoute = if (isOnboardingCompleted()) Route.Auth else Route.Onboarding
        )
    )
    val state: StateFlow<MainState> = _state.asStateFlow()
}