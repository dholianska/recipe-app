package com.holianska.recipeapp.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.holianska.recipeapp.domain.PasswordError
import com.holianska.recipeapp.domain.UsernameError
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import recipeapp.shared.generated.resources.*

@Composable
fun AuthScreen(
    onSignedIn: () -> Unit,
    viewModel: AuthViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                AuthEffect.SignedIn -> onSignedIn()
            }
        }
    }

    AuthContent(state = state, onIntent = viewModel::onIntent)
}

@Composable
private fun AuthContent(
    state: AuthState,
    onIntent: (AuthIntent) -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        // Кольорова шапка
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp))
                .background(MaterialTheme.colorScheme.primary)
                .statusBarsPadding()
                .padding(horizontal = 24.dp, vertical = 40.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = stringResource(Res.string.auth_title),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary,
            )
            Text(
                text = stringResource(Res.string.auth_subtitle),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f),
            )
        }

        // Форма
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
                .imePadding()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            OutlinedTextField(
                value = state.username,
                onValueChange = { onIntent(AuthIntent.UsernameChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(Res.string.auth_username)) },
                singleLine = true,
                isError = state.usernameError != null,
                supportingText = state.usernameError?.let { error ->
                    { Text(stringResource(error.message())) }
                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    autoCorrectEnabled = false,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                ),
                // Next на полі імені → фокус переходить на пароль
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Next) }
                ),
                shape = MaterialTheme.shapes.medium,
            )

            OutlinedTextField(
                value = state.password,
                onValueChange = { onIntent(AuthIntent.PasswordChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(Res.string.auth_password)) },
                singleLine = true,
                isError = state.passwordError != null,
                supportingText = state.passwordError?.let { error ->
                    { Text(stringResource(error.message())) }
                },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                ),
                // Done на полі пароля → та сама дія, що й кнопка "Увійти"
                keyboardActions = KeyboardActions(
                    onDone = { onIntent(AuthIntent.SignIn) }
                ),
                shape = MaterialTheme.shapes.medium,
            )

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = { onIntent(AuthIntent.SignIn) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
            ) {
                Text(
                    text = stringResource(Res.string.auth_sign_in),
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
    }
}

private fun UsernameError.message(): StringResource = when (this) {
    UsernameError.Empty -> Res.string.auth_error_username_empty
    UsernameError.TooShort -> Res.string.auth_error_username_short
}

private fun PasswordError.message(): StringResource = when (this) {
    PasswordError.Empty -> Res.string.auth_error_password_empty
    PasswordError.TooShort -> Res.string.auth_error_password_short
}