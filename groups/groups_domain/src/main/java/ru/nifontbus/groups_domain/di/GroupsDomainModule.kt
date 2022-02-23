package ru.nifontbus.groups_domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import ru.nifontbus.groups_domain.repository.GroupsRepository
import ru.nifontbus.groups_domain.use_cases.*

@Module
@InstallIn(ViewModelComponent::class)
object GroupsDomainModule {

    @Provides
    @ViewModelScoped
    fun provideGroupsUseCases(repository: GroupsRepository) = GroupsUseCases(
        getGroups = GetGroups(repository),
        getGroupById = GetGroupById(repository),
        setCurrentGroup = SetCurrentGroup(repository),
        getCurrentGroup = GetCurrentGroup(repository),
        syncGroups = SyncGroups(repository)
    )
}