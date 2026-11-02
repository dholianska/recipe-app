package com.holianska.recipeapp.data.network

object ApiConfig {
    const val BASE_URL = "https://dummyjson.com"

    // Час життя accessToken. 1 хвилина зручна, щоб побачити автооновлення.
    // Для звичайної роботи можна поставити 30.
    const val TOKEN_LIFETIME_MINUTES = 1
}