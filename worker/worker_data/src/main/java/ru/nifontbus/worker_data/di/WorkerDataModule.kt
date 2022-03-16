package ru.nifontbus.worker_data.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.nifontbus.worker_data.repository.WorkerRepositoryImpl
import ru.nifontbus.worker_domain.repository.WorkerRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WorkerDataModule {

    @Provides
    @Singleton
    fun provideManager(
        @ApplicationContext context: Context
    ): WorkerRepository = WorkerRepositoryImpl(context = context)
}
