package com.holianska.recipeapp.di

import com.holianska.recipeapp.data.OnboardingRepositoryImpl
import com.holianska.recipeapp.domain.OnboardingRepository
import com.holianska.recipeapp.domain.usecase.IsOnboardingCompletedUseCase
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

val appModule = module {
    single<Settings> { Settings() }
    single<OnboardingRepository> { OnboardingRepositoryImpl(get()) }

    factoryOf(::IsOnboardingCompletedUseCase)
    factoryOf(::SetOnboardingCompletedUseCase)
    factoryOf(::ValidateUsernameUseCase)
    factoryOf(::ValidatePasswordUseCase)

    viewModelOf(::MainViewModel)
    viewModelOf(::OnboardingViewModel)
    viewModelOf(::AuthViewModel)
}