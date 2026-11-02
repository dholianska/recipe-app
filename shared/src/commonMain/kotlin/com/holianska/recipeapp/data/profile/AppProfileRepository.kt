package com.holianska.recipeapp.data.profile

import co.touchlab.kermit.Logger
import com.holianska.recipeapp.data.network.ApiException
import com.holianska.recipeapp.data.profile.service.ProfileApiService
import com.holianska.recipeapp.domain.profile.ProfileRepository
import com.holianska.recipeapp.domain.profile.User
import com.holianska.recipeapp.domain.profile.UserResult
import kotlin.coroutines.cancellation.CancellationException

class AppProfileRepository(private val api: ProfileApiService) : ProfileRepository {

    override suspend fun getCurrentUser(): UserResult =
        try {
            val response = api.getMe()
            UserResult.Success(User(response.username, response.firstName, response.lastName))
        } catch (e: CancellationException) {
            throw e
        } catch (e: ApiException) {
            // 401/403 після невдалого оновлення токена означає, що сесія закінчилась
            if (e.statusCode == 401 || e.statusCode == 403) UserResult.Unauthorized
            else UserResult.Failure
        } catch (e: Exception) {
            Logger.e(tag = "Profile", throwable = e) { "Failed to load profile" }
            UserResult.Failure
        }
}