package ru.nifontbus.events_domain.use_cases

import kotlinx.coroutines.flow.StateFlow
import ru.nifontbus.events_domain.model.Event
import ru.nifontbus.events_domain.repository.EventsRepository

class GetEvents(
    private val repository: EventsRepository
) {
    operator fun invoke(): StateFlow<List<Event>> = repository.events
}