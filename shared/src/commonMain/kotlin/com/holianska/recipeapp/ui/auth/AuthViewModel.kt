package com.holianska.recipeapp.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.holianska.recipeapp.domain.auth.LoginResult
import com.holianska.recipeapp.domain.usecase.LoginUseCase
import com.holianska.recipeapp.domain.usecase.ValidatePasswordUseCase
import com.holianska.recipeapp.domain.usecase.ValidateUsernameUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val validateUsername: ValidateUsernameUseCase,
    private val validatePassword: ValidatePasswordUseCase,
    private val login: LoginUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state.asStateFlow()

    private val _effects = Channel<AuthEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    fun onIntent(intent: AuthIntent) {
        when (intent) {
            is AuthIntent.UsernameChanged ->
                _state.update { it.copy(username = intent.value, usernameError = null) }
            is AuthIntent.PasswordChanged ->
                _state.update { it.copy(password = intent.value, passwordError = null) }
            AuthIntent.SignIn -> signIn()
        }
    }

    private fun signIn() {
        val current = _state.value
        if (current.isLoading) return // захист від подвійного натискання

        val usernameError = validateUsername(current.username)
        val passwordError = validatePassword(current.password)
        _state.update { it.copy(usernameError = usernameError, passwordError = passwordError) }
        if (usernameError != null || passwordError != null) return

        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            when (val result = login(current.username, current.password)) {
                LoginResult.Success -> {
                    Logger.i(tag = "Auth") { "Signed in" }
                    _effects.send(AuthEffect.SignedIn)
                }
                is LoginResult.Failure -> _effects.send(AuthEffect.ShowError(result.error))
            }
            _state.update { it.copy(isLoading = false) }
        }
    }
}