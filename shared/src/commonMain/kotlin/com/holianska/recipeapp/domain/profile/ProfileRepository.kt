package com.holianska.recipeapp.domain.profile

data class User(
    val username: String,
    val firstName: String,
    val lastName: String,
) {
    val displayName: String get() = firstName.ifBlank { username }
}

sealed interface UserResult {
    data class Success(val user: User) : UserResult
    data object Unauthorized : UserResult
    data object Failure : UserResult
}

interface ProfileRepository {
    suspend fun getCurrentUser(): UserResult
}