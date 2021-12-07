package ru.nifontbus.contactsevents.di

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.nifontbus.contactsevents.domain.repository.ContactsRepository
import ru.nifontbus.contactsevents.domain.repository.SettingsRepo
import ru.nifontbus.contactsevents.domain.use_cases.events.*
import ru.nifontbus.contactsevents.domain.use_cases.groups.GetGroupById
import ru.nifontbus.contactsevents.domain.use_cases.groups.GetGroups
import ru.nifontbus.contactsevents.domain.use_cases.groups.GroupsUseCases
import ru.nifontbus.contactsevents.domain.use_cases.persons.*
import ru.nifontbus.contactsevents.domain.use_cases.settings.GetCurrentGroup
import ru.nifontbus.contactsevents.domain.use_cases.settings.SetCurrentGroup
import ru.nifontbus.contactsevents.domain.use_cases.settings.SettingsUseCases
import javax.inject.Singleton

// https://howtodoandroid.com/android-hilt-dependency-injection/

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @RequiresApi(Build.VERSION_CODES.O)
    @Provides
    @Singleton
    fun provideContacts(
        @ApplicationContext context: Context
    ): ContactsRepository = ContactsRepository(context)

    @Provides
    @Singleton
    fun provideSettings(
        @ApplicationContext context: Context
    ): SettingsRepo = SettingsRepo(context)

    @ExperimentalCoroutinesApi
    @Provides
    @Singleton
    fun provideEventUseCases(repository: ContactsRepository): EventsUseCases {
        return EventsUseCases(
            addEvent = AddEvent(repository),
            getEvents = GetEvents(repository),
            getSortedEvents = GetSortedEvents(repository),
            getEventsByPerson = GetEventsByPerson(repository),
            deleteEvent = DeleteEvent(repository)
        )
    }

/*    @Provides
    @Singleton
    fun provideTemplatesUseCases(repository: FirebaseRepo): TemplatesUseCases {
        return TemplatesUseCases(
            addTemplate = AddTemplate(repository),
            getTemplates = GetTemplates(repository),
            deleteTemplate = DeleteTemplate(repository)
        )
    }*/


    @Provides
    @Singleton
    fun providePersonsUseCases(repository: ContactsRepository) =
        PersonsUseCases(
//            addPerson = AddPerson(repository),
            getPersons = GetPersons(repository),
            getPersonById = GetPersonById(repository),
            getPersonByIdFlow = GetPersonByIdFlow(repository),
//            deletePerson = DeletePerson(repository),
            getPersonsFromGroup = GetPersonsFromGroup(repository),
            getPersonsFilteredFromGroup = GetPersonsFilteredFromGroup(repository),
//            getPersonAge = GetPersonAge(repository),
//            updatePerson = UpdatePerson(repository),
//            deletePersonWithEvents = DeletePersonWithEvents(repository)
            getPersonInfo = GetPersonInfo(repository)
        )

    @Provides
    @Singleton
    fun provideGroupsUseCases(repository: ContactsRepository) = GroupsUseCases(
//        addGroup = AddGroup(repository),
        getGroups = GetGroups(repository),
        getGroupById = GetGroupById(repository),
//        deleteGroup = DeleteGroup(repository),
//        updateGroup = UpdateGroup(repository)
    )

    @Provides
    @Singleton
    fun provideSettingsUseCases(repository: SettingsRepo) = SettingsUseCases(
        setCurrentGroup = SetCurrentGroup(repository),
        getCurrentGroup = GetCurrentGroup(repository),
    )
}