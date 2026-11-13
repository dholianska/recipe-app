package com.holianska.recipeapp.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import com.holianska.recipeapp.ui.common.CenteredScrollColumn
import com.holianska.recipeapp.ui.common.isTwoPaneLayout
import com.holianska.recipeapp.domain.PasswordError
import com.holianska.recipeapp.domain.UsernameError
import com.holianska.recipeapp.domain.auth.LoginError
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import recipeapp.shared.generated.resources.*


private const val AuthBrandPaneWeight = 0.45f
@Composable
fun AuthScreen(
    onSignedIn: () -> Unit,
    viewModel: AuthViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Тексти беремо тут, бо stringResource можна викликати лише в composable-коді
    val invalidCredentialsText = stringResource(Res.string.auth_error_invalid_credentials)
    val networkText = stringResource(Res.string.auth_error_network)
    val unknownText = stringResource(Res.string.auth_error_unknown)

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                AuthEffect.SignedIn -> onSignedIn()
                is AuthEffect.ShowError -> {
                    val message = when (effect.error) {
                        LoginError.InvalidCredentials -> invalidCredentialsText
                        LoginError.Network -> networkText
                        LoginError.Unknown -> unknownText
                    }
                    snackbarHostState.showSnackbar(message)
                }
            }
        }
    }

    Box(Modifier.fillMaxSize()) {
        AuthContent(state = state, onIntent = viewModel::onIntent)
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .imePadding()
                .padding(16.dp),
        )
    }
}

@Composable
private fun AuthContent(
    state: AuthState,
    onIntent: (AuthIntent) -> Unit,
) {
    val form: @Composable () -> Unit = { AuthForm(state = state, onIntent = onIntent) }

    if (isTwoPaneLayout()) {
        AuthTwoPane(form = form)
    } else {
        AuthSinglePane(form = form)
    }
}

// Вузький екран: заголовок зверху, форма під ним (як було)
@Composable
private fun AuthSinglePane(form: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        AuthHeader(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp))
                .background(MaterialTheme.colorScheme.primary)
                .statusBarsPadding()
                .padding(horizontal = 24.dp, vertical = 40.dp),
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
                .imePadding()
                .padding(24.dp),
        ) {
            form()
        }
    }
}

// Широкий екран: ліворуч панель із заголовком, праворуч форма
@Composable
private fun AuthTwoPane(form: @Composable () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Box(
            modifier = Modifier
                .weight(AuthBrandPaneWeight)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.primary)
                .windowInsetsPadding(
                    WindowInsets.safeContent.only(WindowInsetsSides.Start + WindowInsetsSides.Vertical)
                )
                .padding(40.dp),
            contentAlignment = Alignment.CenterStart,
        ) {
            AuthHeader(large = true, modifier = Modifier.widthIn(max = 440.dp))
        }
        Box(
            modifier = Modifier
                .weight(1f - AuthBrandPaneWeight)
                .fillMaxHeight()
                .windowInsetsPadding(
                    WindowInsets.safeContent.only(WindowInsetsSides.End + WindowInsetsSides.Vertical)
                ),
            contentAlignment = Alignment.Center,
        ) {
            CenteredScrollColumn(
                spacing = 16.dp,
                modifier = Modifier.widthIn(max = 520.dp),
                contentPadding = PaddingValues(40.dp),
            ) {
                form()
            }
        }
    }
}

@Composable
private fun AuthHeader(
    modifier: Modifier = Modifier,
    large: Boolean = false,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = stringResource(Res.string.auth_title),
            style = if (large) MaterialTheme.typography.displaySmall
            else MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary,
        )
        Text(
            text = stringResource(Res.string.auth_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f),
        )
    }
}

// Форма однакова в обох режимах (поля, валідація, дії клавіатури, кнопка)
@Composable
private fun AuthForm(
    state: AuthState,
    onIntent: (AuthIntent) -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        OutlinedTextField(
            value = state.username,
            onValueChange = { onIntent(AuthIntent.UsernameChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading,
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
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Next) }
            ),
            shape = MaterialTheme.shapes.medium,
        )

        OutlinedTextField(
            value = state.password,
            onValueChange = { onIntent(AuthIntent.PasswordChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading,
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
            keyboardActions = KeyboardActions(
                onDone = { onIntent(AuthIntent.SignIn) }
            ),
            shape = MaterialTheme.shapes.medium,
        )

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = { onIntent(AuthIntent.SignIn) },
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth().height(56.dp),
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            } else {
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