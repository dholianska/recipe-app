package com.holianska.recipeapp.domain.usecase

import com.holianska.recipeapp.domain.auth.AuthRepository
import com.holianska.recipeapp.domain.auth.LoginResult

class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(username: String, password: String): LoginResult =
        repository.login(username.trim(), password)
}