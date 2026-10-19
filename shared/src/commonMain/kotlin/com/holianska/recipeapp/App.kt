package com.holianska.recipeapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import co.touchlab.kermit.Logger
import com.holianska.recipeapp.settings.AppSettings
import com.holianska.recipeapp.ui.onboarding.OnboardingScreen
import com.holianska.recipeapp.ui.theme.RecipeTheme
import org.jetbrains.compose.resources.stringResource
import recipeapp.shared.generated.resources.Res
import recipeapp.shared.generated.resources.home_reset
import recipeapp.shared.generated.resources.home_title

@Composable
@Preview
fun App() {
    RecipeTheme {
        val appSettings = remember { AppSettings() }
        var onboardingDone by remember { mutableStateOf(appSettings.onboardingShown) }

        if (!onboardingDone) {
            OnboardingScreen(
                onFinish = {
                    appSettings.onboardingShown = true
                    onboardingDone = true
                    Logger.i(tag = "Onboarding") { "Onboarding finished" }
                }
            )
        } else {
            HomeScreen(
                onResetOnboarding = {
                    appSettings.onboardingShown = false
                    onboardingDone = false
                    Logger.i(tag = "Onboarding") { "Onboarding reset" }
                }
            )
        }
    }
}

@Composable
private fun HomeScreen(onResetOnboarding: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeContentPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(Res.string.home_title),
            style = MaterialTheme.typography.headlineLarge,
        )
        Button(onClick = onResetOnboarding) {
            Text(stringResource(Res.string.home_reset))
        }
    }
}