package ru.nifontbus.contactsevents.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.nifontbus.contactsevents.domain.repository.ContactsRepository
import ru.nifontbus.contactsevents.domain.use_cases.groups.GetGroupById
import ru.nifontbus.contactsevents.domain.use_cases.groups.GetGroups
import ru.nifontbus.contactsevents.domain.use_cases.groups.GroupsUseCases
import ru.nifontbus.contactsevents.domain.use_cases.persons.*
import ru.nifontbus.contactsevents.domain.use_cases.template.GetTemplates
import ru.nifontbus.contactsevents.domain.use_cases.template.TemplatesUseCases
import ru.nifontbus.settings_domain.repository.SettingsRepository
import javax.inject.Singleton

// https://howtodoandroid.com/android-hilt-dependency-injection/

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideContacts(
        @ApplicationContext context: Context
    ): ContactsRepository = ContactsRepository(context)

    @Provides
    @Singleton
    fun provideTemplatesUseCases(
        repository: ContactsRepository,
        settingsRepository: SettingsRepository
    ): TemplatesUseCases {
        return TemplatesUseCases(
            getTemplates = GetTemplates(repository, settingsRepository),
        )
    }


    @Provides
    @Singleton
    fun providePersonsUseCases(repository: ContactsRepository) =
        PersonsUseCases(
            getPersons = GetPersons(repository),
            getPersonById = GetPersonById(repository),
            getPersonByIdFlow = GetPersonByIdFlow(repository),
            getPersonsFromGroup = GetPersonsFromGroup(repository),
            getPersonsFilteredFromGroup = GetPersonsFilteredFromGroup(repository),
            getPersonInfo = GetPersonInfo(repository),
            getPhotoById = GetPhotoById(repository),
            getDisplayPhoto = GetDisplayPhoto(repository)
        )

    @Provides
    @Singleton
    fun provideGroupsUseCases(repository: ContactsRepository) = GroupsUseCases(
        getGroups = GetGroups(repository),
        getGroupById = GetGroupById(repository),
    )
}