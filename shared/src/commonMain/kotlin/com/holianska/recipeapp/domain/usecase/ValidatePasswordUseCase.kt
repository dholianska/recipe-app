package com.holianska.recipeapp.domain.usecase

import com.holianska.recipeapp.domain.PasswordError

class ValidatePasswordUseCase {
    operator fun invoke(password: String): PasswordError? = when {
        password.isEmpty() -> PasswordError.Empty
        password.length < MIN_LENGTH -> PasswordError.TooShort
        else -> null
    }

    private companion object { const val MIN_LENGTH = 6 }
}