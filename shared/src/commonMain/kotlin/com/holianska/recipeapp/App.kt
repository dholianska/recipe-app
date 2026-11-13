package com.holianska.recipeapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.holianska.recipeapp.di.appModule
import com.holianska.recipeapp.di.rememberPlatformModule
import com.holianska.recipeapp.navigation.AppNavigation
import com.holianska.recipeapp.ui.main.MainViewModel
import com.holianska.recipeapp.ui.theme.RecipeTheme
import org.koin.compose.KoinApplication
import org.koin.compose.viewmodel.koinViewModel
import org.koin.dsl.koinConfiguration

@Composable
@Preview
fun App() {
    val platformModule = rememberPlatformModule()
    val koinConfig = remember(platformModule) {
        koinConfiguration { modules(platformModule, appModule) }
    }

    KoinApplication(koinConfig) {
        RecipeTheme {
            val mainViewModel = koinViewModel<MainViewModel>()
            val state by mainViewModel.state.collectAsState()
            state.startRoute?.let { AppNavigation(startRoute = it) }
        }
    }
}