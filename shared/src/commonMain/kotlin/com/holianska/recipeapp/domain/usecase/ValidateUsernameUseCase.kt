package com.holianska.recipeapp.domain.usecase

import com.holianska.recipeapp.domain.UsernameError

class ValidateUsernameUseCase {
    operator fun invoke(username: String): UsernameError? = when {
        username.isBlank() -> UsernameError.Empty
        username.trim().length < MIN_LENGTH -> UsernameError.TooShort
        else -> null
    }

    private companion object { const val MIN_LENGTH = 3 }
}