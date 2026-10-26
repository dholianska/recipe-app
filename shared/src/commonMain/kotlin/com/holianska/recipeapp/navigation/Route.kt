package com.holianska.recipeapp.navigation

sealed interface Route {
    data object Onboarding : Route
    data object Auth : Route
    data object Home : Route
}