package com.holianska.recipeapp.domain.usecase

import com.holianska.recipeapp.domain.profile.ProfileRepository
import com.holianska.recipeapp.domain.profile.UserResult

class GetCurrentUserUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke(): UserResult = repository.getCurrentUser()
}