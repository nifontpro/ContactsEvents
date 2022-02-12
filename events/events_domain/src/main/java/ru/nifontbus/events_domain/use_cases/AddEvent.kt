package ru.nifontbus.events_domain.use_cases

import ru.nifontbus.events_domain.model.Event
import ru.nifontbus.events_domain.repository.EventsRepository

class AddEvent(
    private val repository: EventsRepository
) {
    suspend operator fun invoke(event: Event) = repository.addEvent(event)
}