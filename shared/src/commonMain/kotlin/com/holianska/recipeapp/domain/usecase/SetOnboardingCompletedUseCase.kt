package com.holianska.recipeapp.domain.usecase

import com.holianska.recipeapp.domain.OnboardingRepository

class SetOnboardingCompletedUseCase(private val repository: OnboardingRepository) {
    operator fun invoke(completed: Boolean = true) = repository.setOnboardingCompleted(completed)
}