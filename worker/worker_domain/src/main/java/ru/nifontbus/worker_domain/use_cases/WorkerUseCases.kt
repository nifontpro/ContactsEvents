package ru.nifontbus.worker_domain.use_cases

data class WorkerUseCases(
    val startWorker: StartWorker,
    val cancelWorks: CancelWorks
)
