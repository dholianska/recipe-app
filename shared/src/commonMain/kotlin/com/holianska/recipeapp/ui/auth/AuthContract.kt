package com.holianska.recipeapp.ui.auth

import com.holianska.recipeapp.domain.PasswordError
import com.holianska.recipeapp.domain.UsernameError
import com.holianska.recipeapp.domain.auth.LoginError

data class AuthState(
    val username: String = "",
    val password: String = "",
    val usernameError: UsernameError? = null,
    val passwordError: PasswordError? = null,
    val isLoading: Boolean = false,
)

sealed interface AuthIntent {
    data class UsernameChanged(val value: String) : AuthIntent
    data class PasswordChanged(val value: String) : AuthIntent
    data object SignIn : AuthIntent
}

sealed interface AuthEffect {
    data object SignedIn : AuthEffect
    data class ShowError(val error: LoginError) : AuthEffect
}