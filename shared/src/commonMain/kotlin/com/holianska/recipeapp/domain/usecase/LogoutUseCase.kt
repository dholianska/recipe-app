package com.holianska.recipeapp.domain.usecase

import com.holianska.recipeapp.domain.auth.AuthRepository

class LogoutUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke() = repository.logout()
}