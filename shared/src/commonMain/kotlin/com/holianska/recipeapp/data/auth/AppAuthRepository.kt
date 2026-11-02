package com.holianska.recipeapp.data.auth

import co.touchlab.kermit.Logger
import com.holianska.recipeapp.data.auth.model.request.LoginRequest
import com.holianska.recipeapp.data.auth.service.AuthApiService
import com.holianska.recipeapp.data.network.ApiConfig
import com.holianska.recipeapp.data.network.ApiException
import com.holianska.recipeapp.data.security.SecureStorage
import com.holianska.recipeapp.domain.auth.AuthRepository
import com.holianska.recipeapp.domain.auth.LoginError
import com.holianska.recipeapp.domain.auth.LoginResult
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.serialization.SerializationException

class AppAuthRepository(
    private val api: AuthApiService,
    private val storage: SecureStorage,
) : AuthRepository {

    override suspend fun login(username: String, password: String): LoginResult =
        try {
            val response = api.login(
                LoginRequest(username, password, ApiConfig.TOKEN_LIFETIME_MINUTES)
            )
            storage.saveTokens(response.accessToken, response.refreshToken)
            LoginResult.Success
        } catch (e: CancellationException) {
            throw e
        } catch (e: ApiException) {
            // dummyjson відповідає 400 на неправильний логін чи пароль
            val error = if (e.statusCode == 400 || e.statusCode == 401) {
                LoginError.InvalidCredentials
            } else {
                LoginError.Unknown
            }
            LoginResult.Failure(error)
        } catch (e: SerializationException) {
            Logger.e(tag = "Auth", throwable = e) { "Unexpected login response" }
            LoginResult.Failure(LoginError.Unknown)
        } catch (e: Exception) {
            Logger.e(tag = "Auth", throwable = e) { "Login failed" }
            LoginResult.Failure(LoginError.Network)
        }

    override suspend fun isAuthorized(): Boolean = storage.getRefreshToken() != null

    override suspend fun logout() = storage.clear()
}