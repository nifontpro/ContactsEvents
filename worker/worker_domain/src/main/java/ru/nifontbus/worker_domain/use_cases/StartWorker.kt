package ru.nifontbus.worker_domain.use_cases

import ru.nifontbus.worker_domain.repository.WorkerRepository


class StartWorker(
    private val repository: WorkerRepository,
) {
    operator fun invoke() = repository.startWorker()
}