package com.holianska.recipeapp.data.profile.service

import com.holianska.recipeapp.data.network.ApiConfig
import com.holianska.recipeapp.data.network.AuthorizedApiClient
import com.holianska.recipeapp.data.network.bodyOrThrow
import com.holianska.recipeapp.data.profile.model.UserResponse

interface ProfileApiService {
    suspend fun getMe(): UserResponse
}

class AppProfileApiService(private val client: AuthorizedApiClient) : ProfileApiService {
    override suspend fun getMe(): UserResponse =
        client.get("${ApiConfig.BASE_URL}/auth/me").bodyOrThrow()
}