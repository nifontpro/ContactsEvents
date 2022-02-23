package ru.nifontbus.persons_data.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.nifontbus.persons_data.repository.PersonsRepositoryImpl
import ru.nifontbus.persons_domain.repository.PersonsRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PersonsDataModule {

    @Provides
    @Singleton
    fun providePersonsRepository(
        @ApplicationContext context: Context,
    ): PersonsRepository = PersonsRepositoryImpl(
        context = context,
    )

}