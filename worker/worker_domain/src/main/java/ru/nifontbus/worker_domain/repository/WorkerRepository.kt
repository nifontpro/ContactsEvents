package ru.nifontbus.worker_domain.repository

interface WorkerRepository {

    fun startWorker()

    fun cancelWorks()

}