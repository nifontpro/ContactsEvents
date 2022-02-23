package ru.nifontbus.events_data.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.nifontbus.events_data.repository.EventsRepositoryImpl
import ru.nifontbus.events_domain.repository.EventsRepository
import ru.nifontbus.settings_domain.service.MetadataService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object EventDataModule {

    @Provides
    @Singleton
    fun provideEventsRepository(
        @ApplicationContext context: Context,
        metadataService: MetadataService
    ): EventsRepository = EventsRepositoryImpl(
        context = context,
        metadataService = metadataService
    )

}