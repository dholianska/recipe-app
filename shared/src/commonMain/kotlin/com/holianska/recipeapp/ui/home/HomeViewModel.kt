package com.holianska.recipeapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.holianska.recipeapp.domain.profile.UserResult
import com.holianska.recipeapp.domain.usecase.GetCurrentUserUseCase
import com.holianska.recipeapp.domain.usecase.LogoutUseCase
import com.holianska.recipeapp.domain.usecase.SetOnboardingCompletedUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getCurrentUser: GetCurrentUserUseCase,
    private val logout: LogoutUseCase,
    private val setOnboardingCompleted: SetOnboardingCompletedUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val _effects = Channel<HomeEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    init {
        loadUser()
    }

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.Retry -> loadUser()
            HomeIntent.SignOut -> viewModelScope.launch {
                logout()
                _effects.send(HomeEffect.NavigateToAuth)
            }
            HomeIntent.ResetOnboarding -> viewModelScope.launch {
                logout()
                setOnboardingCompleted(false)
                _effects.send(HomeEffect.NavigateToOnboarding)
            }
        }
    }

    private fun loadUser() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, loadFailed = false) }
            when (val result = getCurrentUser()) {
                is UserResult.Success ->
                    _state.update { it.copy(isLoading = false, user = result.user) }
                UserResult.Unauthorized -> {
                    // Сесія недійсна: чистимо токени й повертаємо на вхід
                    logout()
                    _effects.send(HomeEffect.NavigateToAuth)
                }
                UserResult.Failure ->
                    _state.update { it.copy(isLoading = false, loadFailed = true) }
            }
        }
    }
}