package ru.nifontbus.events_domain.use_cases

import ru.nifontbus.events_domain.repository.EventsRepository

class SyncEvents(
    private val eventsRepository: EventsRepository
) {
    operator fun invoke() = eventsRepository.syncEvents()
}