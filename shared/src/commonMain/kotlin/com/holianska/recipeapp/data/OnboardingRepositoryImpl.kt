package com.holianska.recipeapp.data

import com.holianska.recipeapp.domain.OnboardingRepository
import com.russhwolf.settings.Settings

class OnboardingRepositoryImpl(private val settings: Settings) : OnboardingRepository {

    override fun isOnboardingCompleted(): Boolean =
        settings.getBoolean(KEY_ONBOARDING_SHOWN, false)

    override fun setOnboardingCompleted(completed: Boolean) {
        settings.putBoolean(KEY_ONBOARDING_SHOWN, completed)
    }

    private companion object {
        const val KEY_ONBOARDING_SHOWN = "onboarding_shown"
    }
}