package ru.nifontbus.settings_data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import ru.nifontbus.settings_data.model.AppSettingsSerializer
import ru.nifontbus.settings_data.repository.SettingsRepositoryImpl
import ru.nifontbus.settings_data.service.MetadataServiceImpl
import ru.nifontbus.settings_domain.repository.AppSettings
import ru.nifontbus.settings_domain.repository.SettingsRepository
import ru.nifontbus.settings_domain.service.MetadataService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SettingsDataModule {
    @Provides
    @Singleton
    fun provideSettingsRepository(
        dataStore: DataStore<AppSettings>
    ): SettingsRepository = SettingsRepositoryImpl(
        dataStore = dataStore
    )

    @Provides
    @Singleton
    fun provideMetadataService(
        @ApplicationContext context: Context
    ): MetadataService = MetadataServiceImpl(context = context)

    @Singleton
    @Provides
    fun provideProtoDataStore(
        @ApplicationContext appContext: Context
    ): DataStore<AppSettings> {
        return DataStoreFactory.create(
            serializer = AppSettingsSerializer,
            produceFile = { appContext.dataStoreFile(DATA_STORE_FILE_NAME) },
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { AppSettings() }
            ),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        )
    }
}
private const val DATA_STORE_FILE_NAME = "settings.json"