package ru.nifontbus.events_domain.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import ru.nifontbus.events_domain.repository.EventsRepository
import ru.nifontbus.events_domain.use_cases.*
import ru.nifontbus.settings_domain.repository.SettingsRepository

@Module
@InstallIn(ViewModelComponent::class)
object EventDomainModule {

    @Provides
    @ViewModelScoped
    fun provideEventUseCases(
        repository: EventsRepository,
        settingsRepo: SettingsRepository,
        @ApplicationContext context: Context
    ): EventsUseCases {
        return EventsUseCases(
            addEvent = AddEvent(repository),
            getEvents = GetEvents(repository),
            getSortedEvents = GetSortedEvents(repository, settingsRepo, context),
            getEventsByPerson = GetEventsByPerson(repository),
            getSortedEventsByPerson = GetSortedEventsByPerson(repository),
            deleteEvent = DeleteEvent(repository),
            getEventById = GetEventById(repository),
            updateEvent = UpdateEvent(repository),
            syncEvents = SyncEvents(repository),
            silentSync = SilentSync(repository)
        )
    }
}