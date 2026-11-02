package com.holianska.recipeapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import com.holianska.recipeapp.data.security.SecureStorage
import com.holianska.recipeapp.di.appModule
import com.holianska.recipeapp.navigation.AppNavigation
import com.holianska.recipeapp.ui.main.MainViewModel
import com.holianska.recipeapp.ui.theme.RecipeTheme
import org.koin.compose.KoinApplication
import org.koin.compose.viewmodel.koinViewModel
import com.holianska.recipeapp.di.rememberPlatformModule
import org.koin.compose.koinInject
import co.touchlab.kermit.Logger
import com.holianska.recipeapp.data.profile.service.ProfileApiService
import com.holianska.recipeapp.domain.usecase.LoginUseCase
import com.holianska.recipeapp.domain.usecase.LogoutUseCase
import kotlinx.coroutines.delay

@Composable
@Preview
fun App() {
    val platformModule = rememberPlatformModule()
    KoinApplication(application = { modules(platformModule, appModule) }) {
        RecipeTheme {
            val login = koinInject<LoginUseCase>()
            val logout = koinInject<LogoutUseCase>()
            val profile = koinInject<ProfileApiService>()
            LaunchedEffect(Unit) {
                Logger.i(tag = "NetTest") { "login = ${login("emilys", "emilyspass")}" }
                Logger.i(tag = "NetTest") { "me #1 = ${runCatching { profile.getMe().username }}" }
                delay(70_000) // чекаємо, поки accessToken (1 хв) закінчиться
                Logger.i(tag = "NetTest") { "me #2 = ${runCatching { profile.getMe().username }}" }
                logout()
            }

            val mainViewModel = koinViewModel<MainViewModel>()
            val state by mainViewModel.state.collectAsState()
            state.startRoute?.let { AppNavigation(startRoute = it) }
        }
    }
}