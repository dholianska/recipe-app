package com.holianska.recipeapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import com.holianska.recipeapp.di.appModule
import com.holianska.recipeapp.navigation.AppNavigation
import com.holianska.recipeapp.ui.main.MainViewModel
import com.holianska.recipeapp.ui.theme.RecipeTheme
import org.koin.compose.KoinApplication
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {
    KoinApplication(application = { modules(appModule) }) {
        RecipeTheme {
            val mainViewModel = koinViewModel<MainViewModel>()
            val state by mainViewModel.state.collectAsState()
            state.startRoute?.let { AppNavigation(startRoute = it) }
        }
    }
}