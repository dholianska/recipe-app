package com.holianska.recipeapp.di

import com.holianska.recipeapp.data.OnboardingRepositoryImpl
import com.holianska.recipeapp.data.auth.AppAuthRepository
import com.holianska.recipeapp.data.auth.service.AppAuthApiService
import com.holianska.recipeapp.data.auth.service.AuthApiService
import com.holianska.recipeapp.data.network.AuthorizedApiClient
import com.holianska.recipeapp.data.network.createHttpClient
import com.holianska.recipeapp.data.profile.service.AppProfileApiService
import com.holianska.recipeapp.data.profile.service.ProfileApiService
import com.holianska.recipeapp.data.security.AppSecureStorage
import com.holianska.recipeapp.data.security.SecureStorage
import com.holianska.recipeapp.domain.OnboardingRepository
import com.holianska.recipeapp.domain.auth.AuthRepository
import com.holianska.recipeapp.domain.usecase.IsAuthorizedUseCase
import com.holianska.recipeapp.domain.usecase.IsOnboardingCompletedUseCase
import com.holianska.recipeapp.domain.usecase.LoginUseCase
import com.holianska.recipeapp.domain.usecase.LogoutUseCase
import com.holianska.recipeapp.domain.usecase.SetOnboardingCompletedUseCase
import com.holianska.recipeapp.domain.usecase.ValidatePasswordUseCase
import com.holianska.recipeapp.domain.usecase.ValidateUsernameUseCase
import com.holianska.recipeapp.ui.auth.AuthViewModel
import com.holianska.recipeapp.ui.main.MainViewModel
import com.holianska.recipeapp.ui.onboarding.OnboardingViewModel
import com.russhwolf.settings.Settings
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import com.holianska.recipeapp.data.profile.AppProfileRepository
import com.holianska.recipeapp.domain.profile.ProfileRepository
import com.holianska.recipeapp.domain.usecase.GetCurrentUserUseCase
import com.holianska.recipeapp.ui.home.HomeViewModel

val appModule = module {
    // Налаштування й онбординг
    single<Settings> { Settings() }
    single<OnboardingRepository> { OnboardingRepositoryImpl(get()) }
    single<ProfileRepository> { AppProfileRepository(get()) }

    // Мережа й безпечне сховище
    single<SecureStorage> { AppSecureStorage(get()) }
    single { createHttpClient() }
    single<AuthApiService> { AppAuthApiService(get()) }
    single { AuthorizedApiClient(get(), get(), get()) }
    single<ProfileApiService> { AppProfileApiService(get()) }
    single<AuthRepository> { AppAuthRepository(get(), get()) }

    // Use case
    factoryOf(::IsOnboardingCompletedUseCase)
    factoryOf(::SetOnboardingCompletedUseCase)
    factoryOf(::ValidateUsernameUseCase)
    factoryOf(::ValidatePasswordUseCase)
    factoryOf(::LoginUseCase)
    factoryOf(::IsAuthorizedUseCase)
    factoryOf(::LogoutUseCase)
    factoryOf(::GetCurrentUserUseCase)

    // ViewModel
    viewModelOf(::MainViewModel)
    viewModelOf(::OnboardingViewModel)
    viewModelOf(::AuthViewModel)
    viewModelOf(::HomeViewModel)
}