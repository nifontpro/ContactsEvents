package ru.nifontbus.settings_domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import ru.nifontbus.settings_domain.repository.SettingsRepository
import ru.nifontbus.settings_domain.use_cases.*
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
object SettingsDomainModule {

    @Provides
    @ViewModelScoped
    fun provideSettingsUseCases(repository: SettingsRepository) = SettingsUseCases(
        getReposeFeatures = GetReposeFeatures(repository),
        getAdd40Day = GetAdd40Day(repository),
        saveReposeFeatures = SaveReposeFeatures(repository),
        saveAdd40Day = SaveAdd40Day(repository)
    )
}