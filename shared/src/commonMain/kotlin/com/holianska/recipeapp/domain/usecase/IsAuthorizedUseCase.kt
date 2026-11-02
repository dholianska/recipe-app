package com.holianska.recipeapp.domain.usecase

import com.holianska.recipeapp.domain.auth.AuthRepository

class IsAuthorizedUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(): Boolean = repository.isAuthorized()
}