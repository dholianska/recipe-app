package com.holianska.recipeapp.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import recipeapp.shared.generated.resources.*

@Composable
fun HomeScreen(
    onNavigateToAuth: () -> Unit,
    onNavigateToOnboarding: () -> Unit,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                HomeEffect.NavigateToAuth -> onNavigateToAuth()
                HomeEffect.NavigateToOnboarding -> onNavigateToOnboarding()
            }
        }
    }

    HomeContent(state = state, onIntent = viewModel::onIntent)
}

@Composable
private fun HomeContent(
    state: HomeState,
    onIntent: (HomeIntent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeContentPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
    ) {
        when {
            state.isLoading -> CircularProgressIndicator()
            state.loadFailed -> {
                Text(
                    text = stringResource(Res.string.home_load_error),
                    style = MaterialTheme.typography.bodyLarge,
                )
                Button(onClick = { onIntent(HomeIntent.Retry) }) {
                    Text(stringResource(Res.string.home_retry))
                }
            }
            state.user != null -> Text(
                text = stringResource(Res.string.home_greeting, state.user.displayName),
                style = MaterialTheme.typography.headlineLarge,
            )
        }

        Button(onClick = { onIntent(HomeIntent.SignOut) }) {
            Text(stringResource(Res.string.home_sign_out))
        }
        OutlinedButton(onClick = { onIntent(HomeIntent.ResetOnboarding) }) {
            Text(stringResource(Res.string.home_reset))
        }
    }
}