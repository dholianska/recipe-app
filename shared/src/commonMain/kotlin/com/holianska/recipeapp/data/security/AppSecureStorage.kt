package com.holianska.recipeapp.data.security

import eu.anifantakis.lib.ksafe.KSafe

class AppSecureStorage(private val ksafe: KSafe) : SecureStorage {

    override suspend fun saveTokens(accessToken: String, refreshToken: String) {
        ksafe.put(KEY_ACCESS, accessToken)
        ksafe.put(KEY_REFRESH, refreshToken)
    }

    override suspend fun getAccessToken(): String? =
        ksafe.get(KEY_ACCESS, "").ifEmpty { null }

    override suspend fun getRefreshToken(): String? =
        ksafe.get(KEY_REFRESH, "").ifEmpty { null }

    // Порожній рядок = "токена немає"
    override suspend fun clear() {
        ksafe.put(KEY_ACCESS, "")
        ksafe.put(KEY_REFRESH, "")
    }

    private companion object {
        const val KEY_ACCESS = "access_token"
        const val KEY_REFRESH = "refresh_token"
    }
}