package com.holianska.recipeapp.data.auth.model.response

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val id: Int,
    val username: String,
    val firstName: String = "",
    val lastName: String = "",
    val accessToken: String,
    val refreshToken: String,
)