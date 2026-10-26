package com.holianska.recipeapp.domain.usecase

import com.holianska.recipeapp.domain.OnboardingRepository

class IsOnboardingCompletedUseCase(private val repository: OnboardingRepository) {
    operator fun invoke(): Boolean = repository.isOnboardingCompleted()
}