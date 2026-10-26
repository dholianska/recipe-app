package com.holianska.recipeapp.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import recipeapp.shared.generated.resources.Res
import recipeapp.shared.generated.resources.home_reset
import recipeapp.shared.generated.resources.home_sign_out
import recipeapp.shared.generated.resources.home_title
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onSignOut: () -> Unit,
    onResetOnboarding: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeContentPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
    ) {
        Text(
            text = stringResource(Res.string.home_title),
            style = MaterialTheme.typography.headlineLarge,
        )
        Button(onClick = onSignOut) { Text(stringResource(Res.string.home_sign_out)) }
        OutlinedButton(onClick = onResetOnboarding) { Text(stringResource(Res.string.home_reset)) }
    }
}