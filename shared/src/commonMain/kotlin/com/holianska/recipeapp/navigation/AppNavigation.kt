package com.holianska.recipeapp.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.holianska.recipeapp.ui.auth.AuthScreen
import com.holianska.recipeapp.ui.home.HomeScreen
import com.holianska.recipeapp.ui.onboarding.OnboardingScreen
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

// Перелік усіх маршрутів, які можуть опинитись у стеку
private val navSavedStateConfig = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(Route.Onboarding::class, Route.Onboarding.serializer())
            subclass(Route.Auth::class, Route.Auth.serializer())
            subclass(Route.Home::class, Route.Home.serializer())
        }
    }
}

@Composable
fun AppNavigation(startRoute: Route) {
    val backStack = rememberNavBackStack(navSavedStateConfig, startRoute)

    NavDisplay(
        backStack = backStack,
        onBack = { if (backStack.size > 1) backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            entry<Route.Onboarding> {
                OnboardingScreen(onFinish = { backStack.resetTo(Route.Auth) })
            }
            entry<Route.Auth> {
                AuthScreen(onSignedIn = { backStack.resetTo(Route.Home) })
            }
            entry<Route.Home> {
                HomeScreen(
                    onSignOut = { backStack.resetTo(Route.Auth) },
                    onResetOnboarding = {
                        // Скидання прапорця онбордингу виконується так само, як і раніше:
                        // залиште тут той код, що був у вашій версії Home
                        backStack.resetTo(Route.Onboarding)
                    },
                )
            }
        },
    )
}

// Повністю замінює стек, щоб "Назад" не повертав на попередній екран
private fun MutableList<NavKey>.resetTo(route: Route) {
    clear()
    add(route)
}