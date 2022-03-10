package ru.nifontbus.worker_domain.repository

import ru.nifontbus.events_domain.model.Event

interface WorkerRepository {
    fun getEventsNow(): List<Event>
}
