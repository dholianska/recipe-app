package com.holianska.recipeapp.data.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

val AppJson = Json { ignoreUnknownKeys = true }

// Перенаправляємо логи Ktor у Kermit
private object KermitHttpLogger : Logger {
    override fun log(message: String) {
        co.touchlab.kermit.Logger.d(tag = "Http") { message }
    }
}

fun createHttpClient(): HttpClient = HttpClient {
    install(ContentNegotiation) { json(AppJson) }
    install(Logging) {
        logger = KermitHttpLogger
        level = LogLevel.INFO // лише рядки запит/статус, без заголовків і токенів
    }
}