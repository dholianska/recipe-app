package com.holianska.recipeapp.data.network

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess

class ApiException(val statusCode: Int) : Exception("HTTP $statusCode")

/** Повертає тіло відповіді або кидає ApiException, якщо статус не 2xx. */
suspend inline fun <reified T> HttpResponse.bodyOrThrow(): T {
    if (!status.isSuccess()) throw ApiException(statusCode = status.value)
    return body()
}