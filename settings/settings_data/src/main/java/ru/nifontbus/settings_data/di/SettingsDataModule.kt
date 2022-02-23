package ru.nifontbus.settings_data.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.nifontbus.settings_data.repository.SettingsRepositoryImpl
import ru.nifontbus.settings_data.service.MetadataServiceImpl
import ru.nifontbus.settings_domain.repository.SettingsRepository
import ru.nifontbus.settings_domain.service.MetadataService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SettingsDataModule {
    @Provides
    @Singleton
    fun provideSettings(
        sharedPreferences: SharedPreferences
    ): SettingsRepository = SettingsRepositoryImpl(sharedPreferences)

    @Provides
    @Singleton
    fun provideSharedPref(app: Application): SharedPreferences {
        return app.getSharedPreferences(
            SHARED_PREF_NAME,
            Context.MODE_PRIVATE
        )
    }

    @Provides
    @Singleton
    fun provideMetadataService(): MetadataService = MetadataServiceImpl()
}

const val SHARED_PREF_NAME = "Current_setting"