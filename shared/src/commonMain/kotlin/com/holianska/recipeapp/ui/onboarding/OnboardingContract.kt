package com.holianska.recipeapp.ui.onboarding

data class OnboardingState(
    val currentPage: Int = 0,
    val pageCount: Int = 3,
) {
    val isLastPage: Boolean get() = currentPage == pageCount - 1
}

sealed interface OnboardingIntent {
    data class PageChanged(val page: Int) : OnboardingIntent
    data object Next : OnboardingIntent
    data object Skip : OnboardingIntent
}

sealed interface OnboardingEffect {
    data object Finished : OnboardingEffect
}