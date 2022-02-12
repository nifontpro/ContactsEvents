package ru.nifontbus.events_domain.use_cases

import ru.nifontbus.events_domain.repository.EventsRepository

class GetEventById(
    private val repository: EventsRepository
) {
    operator fun invoke(id: Long) = repository.events.value.find { it.id == id }
}