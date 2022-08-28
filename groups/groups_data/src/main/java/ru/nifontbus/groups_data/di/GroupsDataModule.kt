package ru.nifontbus.groups_data.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
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
        @ApplicationContext context: Context,
        sharedPreferences: SharedPreferences
    ): GroupsRepository = GroupsRepositoryImpl(context, sharedPreferences)

    @Provides
    @Singleton
    fun provideSharedPref(app: Application): SharedPreferences {
        return app.getSharedPreferences(
            SHARED_PREF_NAME,
            Context.MODE_PRIVATE
        )
    }
}

private const val SHARED_PREF_NAME = "Current_setting"