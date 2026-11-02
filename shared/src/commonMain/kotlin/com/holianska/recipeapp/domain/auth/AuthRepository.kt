package com.holianska.recipeapp.domain.auth

enum class LoginError { InvalidCredentials, Network, Unknown }

sealed interface LoginResult {
    data object Success : LoginResult
    data class Failure(val error: LoginError) : LoginResult
}

interface AuthRepository {
    suspend fun login(username: String, password: String): LoginResult
    suspend fun isAuthorized(): Boolean
    suspend fun logout()
}