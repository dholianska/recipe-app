package com.holianska.recipeapp.settings

import com.russhwolf.settings.Settings

class AppSettings(private val settings: Settings = Settings()) {

    var onboardingShown: Boolean
        get() = settings.getBoolean(KEY_ONBOARDING_SHOWN, false)
        set(value) = settings.putBoolean(KEY_ONBOARDING_SHOWN, value)

    private companion object {
        const val KEY_ONBOARDING_SHOWN = "onboarding_shown"
    }
}