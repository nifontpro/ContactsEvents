package ru.nifontbus.groups_domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import ru.nifontbus.groups_domain.repository.GroupsRepository
import ru.nifontbus.groups_domain.use_cases.GetGroupById
import ru.nifontbus.groups_domain.use_cases.GetGroups
import ru.nifontbus.groups_domain.use_cases.GroupsUseCases
import javax.inject.Singleton

// https://howtodoandroid.com/android-hilt-dependency-injection/

@Module
@InstallIn(ViewModelComponent::class)
object GroupsDomainModule {

    @Provides
    @ViewModelScoped
    fun provideGroupsUseCases(repository: GroupsRepository) = GroupsUseCases(
        getGroups = GetGroups(repository),
        getGroupById = GetGroupById(repository),
    )
}