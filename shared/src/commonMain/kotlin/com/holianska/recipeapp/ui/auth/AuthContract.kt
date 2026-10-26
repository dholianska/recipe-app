package com.holianska.recipeapp.ui.auth

import com.holianska.recipeapp.domain.PasswordError
import com.holianska.recipeapp.domain.UsernameError

data class AuthState(
    val username: String = "",
    val password: String = "",
    val usernameError: UsernameError? = null,
    val passwordError: PasswordError? = null,
)

sealed interface AuthIntent {
    data class UsernameChanged(val value: String) : AuthIntent
    data class PasswordChanged(val value: String) : AuthIntent
    data object SignIn : AuthIntent
}

sealed interface AuthEffect {
    data object SignedIn : AuthEffect
}