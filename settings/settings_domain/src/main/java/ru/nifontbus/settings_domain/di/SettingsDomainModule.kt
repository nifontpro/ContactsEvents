package ru.nifontbus.settings_domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import ru.nifontbus.settings_domain.repository.SettingsRepository
import ru.nifontbus.settings_domain.service.MetadataService
import ru.nifontbus.settings_domain.use_cases.*

@Module
@InstallIn(ViewModelComponent::class)
object SettingsDomainModule {

    @Provides
    @ViewModelScoped
    fun provideSettingsUseCases(
        repository: SettingsRepository,
    ) = SettingsUseCases(
        getNotificationState = GetNotificationState(repository),
        getReposeFeatures = GetReposeFeatures(repository),
        getAdd40Day = GetAdd40Day(repository),
        saveNotificationState = SaveNotificationState(repository),
        saveReposeFeatures = SaveReposeFeatures(repository),
        saveAdd40Day = SaveAdd40Day(repository),
    )

    @Provides
    @ViewModelScoped
    fun provideMetadataUseCases(
        metadataService: MetadataService
    ) = MetadataUseCases(
        sendMessage = SendMessage(metadataService),
        subscribeMessage = SubscribeMessage(metadataService),
        sendEvent = SendEvent(metadataService),
        getEvent = GetEvent(metadataService),
        resetSyncTime = ResetSyncTime(metadataService)
    )
}