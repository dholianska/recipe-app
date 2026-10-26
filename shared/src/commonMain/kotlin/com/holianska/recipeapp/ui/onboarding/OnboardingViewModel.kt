package com.holianska.recipeapp.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.holianska.recipeapp.domain.usecase.SetOnboardingCompletedUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val setOnboardingCompleted: SetOnboardingCompletedUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(OnboardingState())
    val state: StateFlow<OnboardingState> = _state.asStateFlow()

    private val _effects = Channel<OnboardingEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    fun onIntent(intent: OnboardingIntent) {
        when (intent) {
            is OnboardingIntent.PageChanged -> _state.update { it.copy(currentPage = intent.page) }
            OnboardingIntent.Next ->
                if (_state.value.isLastPage) finish()
                else _state.update { it.copy(currentPage = it.currentPage + 1) }
            OnboardingIntent.Skip -> finish()
        }
    }

    private fun finish() {
        setOnboardingCompleted(true)
        Logger.i(tag = "Onboarding") { "Onboarding finished" }
        viewModelScope.launch { _effects.send(OnboardingEffect.Finished) }
    }
}