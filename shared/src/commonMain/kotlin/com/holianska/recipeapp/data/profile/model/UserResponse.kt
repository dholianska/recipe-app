package com.holianska.recipeapp.data.profile.model

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val id: Int,
    val username: String,
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
)