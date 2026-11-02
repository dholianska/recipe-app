package com.holianska.recipeapp.data.network

import co.touchlab.kermit.Logger
import com.holianska.recipeapp.data.auth.model.request.RefreshRequest
import com.holianska.recipeapp.data.auth.service.AuthApiService
import com.holianska.recipeapp.data.security.SecureStorage
import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Усі запити, що потребують входу, йдуть через цей клас.
 * Якщо сервер відповів 401/403, ми один раз оновлюємо токени й повторюємо запит.
 */
class AuthorizedApiClient(
    private val client: HttpClient,
    private val storage: SecureStorage,
    private val authApi: AuthApiService,
) {
    // Щоб кілька паралельних запитів не оновлювали токен одночасно
    private val refreshMutex = Mutex()

    suspend fun get(url: String): HttpResponse = execute { token ->
        client.get(url) { bearerAuth(token) }
    }

    private suspend fun execute(call: suspend (accessToken: String) -> HttpResponse): HttpResponse {
        val token = storage.getAccessToken()
            ?: throw ApiException(HttpStatusCode.Unauthorized.value)

        val response = call(token)
        if (!response.isAuthError()) return response

        val freshToken = refreshAccessToken(failedToken = token) ?: return response
        return call(freshToken)
    }

    private suspend fun refreshAccessToken(failedToken: String): String? = refreshMutex.withLock {
        // Можливо, інший запит уже оновив токен, поки ми чекали на мьютекс
        val current = storage.getAccessToken()
        if (current != null && current != failedToken) return current

        val refreshToken = storage.getRefreshToken() ?: return null
        try {
            val new = authApi.refresh(RefreshRequest(refreshToken, ApiConfig.TOKEN_LIFETIME_MINUTES))
            // Ротація: сервер видає нову пару, зберігаємо обидва токени
            storage.saveTokens(new.accessToken, new.refreshToken)
            Logger.i(tag = "Auth") { "Tokens refreshed" }
            new.accessToken
        } catch (e: CancellationException) {
            throw e
        } catch (e: ApiException) {
            // Сервер відхилив refresh-токен: сесія закінчилась
            Logger.w(tag = "Auth") { "Refresh rejected (${e.statusCode}), clearing session" }
            storage.clear()
            null
        }
        // Інші помилки (наприклад, нема мережі) летять далі, але сесію не скидають
    }

    private fun HttpResponse.isAuthError() =
        status == HttpStatusCode.Unauthorized || status == HttpStatusCode.Forbidden
}