package ru.nifontbus.persons_domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import ru.nifontbus.persons_domain.repository.PersonsRepository
import ru.nifontbus.persons_domain.use_cases.*

@Module
@InstallIn(ViewModelComponent::class)
object PersonsDomainModule {

    @Provides
    @ViewModelScoped
    fun providePersonsUseCases(repository: PersonsRepository) =
        PersonsUseCases(
            getPersons = GetPersons(repository),
            getPersonById = GetPersonById(repository),
            getPersonByIdFlow = GetPersonByIdFlow(repository),
            getPersonsFromGroup = GetPersonsFromGroup(repository),
            getPersonsFilteredFromGroup = GetPersonsFilteredFromGroup(repository),
            getPersonInfo = GetPersonInfo(repository),
            getDisplayPhoto = GetDisplayPhoto(repository),
            syncPersons = SyncPersons(repository)
        )

}