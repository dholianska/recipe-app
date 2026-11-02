package com.holianska.recipeapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.holianska.recipeapp.domain.usecase.IsAuthorizedUseCase
import com.holianska.recipeapp.domain.usecase.IsOnboardingCompletedUseCase
import com.holianska.recipeapp.navigation.Route
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MainState(val startRoute: Route? = null)

class MainViewModel(
    private val isOnboardingCompleted: IsOnboardingCompletedUseCase,
    private val isAuthorized: IsAuthorizedUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state.asStateFlow()

    init {
        // Читання з KSafe асинхронне, тому стартовий екран визначаємо в корутині
        viewModelScope.launch {
            val route = when {
                isAuthorized() -> Route.Home
                isOnboardingCompleted() -> Route.Auth
                else -> Route.Onboarding
            }
            _state.value = MainState(startRoute = route)
        }
    }
}