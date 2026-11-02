package com.holianska.recipeapp.data.auth.service

import com.holianska.recipeapp.data.auth.model.request.LoginRequest
import com.holianska.recipeapp.data.auth.model.request.RefreshRequest
import com.holianska.recipeapp.data.auth.model.response.LoginResponse
import com.holianska.recipeapp.data.auth.model.response.RefreshResponse
import com.holianska.recipeapp.data.network.ApiConfig
import com.holianska.recipeapp.data.network.bodyOrThrow
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

interface AuthApiService {
    suspend fun login(request: LoginRequest): LoginResponse
    suspend fun refresh(request: RefreshRequest): RefreshResponse
}

class AppAuthApiService(private val client: HttpClient) : AuthApiService {

    override suspend fun login(request: LoginRequest): LoginResponse =
        client.post("${ApiConfig.BASE_URL}/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.bodyOrThrow()

    override suspend fun refresh(request: RefreshRequest): RefreshResponse =
        client.post("${ApiConfig.BASE_URL}/auth/refresh") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.bodyOrThrow()
}