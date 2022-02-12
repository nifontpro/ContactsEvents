package ru.nifontbus.settings_data.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.nifontbus.settings_data.repository.SettingsRepositoryImpl
import ru.nifontbus.settings_domain.repository.SettingsRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SettingsDataModule {
    @Provides
    @Singleton
    fun provideSettings(
        @ApplicationContext context: Context
    ): SettingsRepository = SettingsRepositoryImpl(context)
}