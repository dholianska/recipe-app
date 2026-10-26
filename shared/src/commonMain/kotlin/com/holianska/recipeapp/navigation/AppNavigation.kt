package com.holianska.recipeapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.holianska.recipeapp.domain.usecase.SetOnboardingCompletedUseCase
import com.holianska.recipeapp.ui.auth.AuthScreen
import com.holianska.recipeapp.ui.home.HomeScreen
import com.holianska.recipeapp.ui.onboarding.OnboardingScreen
import org.koin.compose.koinInject

@Composable
fun AppNavigation(startRoute: Route) {
    val backStack = remember { mutableStateListOf<Route>(startRoute) }
    val setOnboardingCompleted = koinInject<SetOnboardingCompletedUseCase>()

    // Повністю замінюємо стек, щоб "Назад" не повертав на попередній екран
    fun replaceWith(route: Route) {
        backStack.clear()
        backStack.add(route)
    }

    NavDisplay(
        backStack = backStack,
        onBack = { if (backStack.size > 1) backStack.removeAt(backStack.lastIndex) },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            entry<Route.Onboarding> {
                OnboardingScreen(onFinish = { replaceWith(Route.Auth) })
            }
            entry<Route.Auth> {
                AuthScreen(onSignedIn = { replaceWith(Route.Home) })
            }
            entry<Route.Home> {
                HomeScreen(
                    onSignOut = { replaceWith(Route.Auth) },
                    onResetOnboarding = {
                        setOnboardingCompleted(false)
                        replaceWith(Route.Onboarding)
                    },
                )
            }
        },
    )
}