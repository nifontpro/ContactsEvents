package ru.nifontbus.groups_data.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.nifontbus.groups_data.repository.GroupsRepositoryImpl
import ru.nifontbus.groups_domain.repository.GroupsRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GroupsDataModule {

    @Provides
    @Singleton
    fun provideGroupsRepository(
        @ApplicationContext context: Context
    ): GroupsRepository = GroupsRepositoryImpl(context)

}