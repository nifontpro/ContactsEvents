package ru.nifontbus.worker_domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import ru.nifontbus.worker_domain.repository.WorkerRepository
import ru.nifontbus.worker_domain.use_cases.CancelWorks
import ru.nifontbus.worker_domain.use_cases.StartWorker
import ru.nifontbus.worker_domain.use_cases.WorkerUseCases

@Module
@InstallIn(ViewModelComponent::class)
object WorkerDomainModule {

    @Provides
    @ViewModelScoped
    fun provideWorkerUseCases(
        workerRepository: WorkerRepository
    ): WorkerUseCases {
        return WorkerUseCases(
            startWorker = StartWorker(workerRepository),
            cancelWorks = CancelWorks(workerRepository)
        )
    }

}