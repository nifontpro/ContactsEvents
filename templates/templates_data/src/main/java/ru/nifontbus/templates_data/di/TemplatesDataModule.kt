package ru.nifontbus.templates_data.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.nifontbus.templates_data.repository.TemplatesRepositoryImpl
import ru.nifontbus.templates_domain.repository.TemplatesRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TemplatesDataModule {

    @Provides
    @Singleton
    fun providePersonsRepository(
        @ApplicationContext context: Context
    ): TemplatesRepository = TemplatesRepositoryImpl(context)

}