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
    fun provideGroupsUseCases(repository: ContactsRepository) = GroupsUseCases(
        getGroups = GetGroups(repository),
        getGroupById = GetGroupById(repository),
    )
}