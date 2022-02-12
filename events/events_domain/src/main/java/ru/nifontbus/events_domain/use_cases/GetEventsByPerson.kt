package ru.nifontbus.events_domain.use_cases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.nifontbus.events_domain.model.Event
import ru.nifontbus.events_domain.repository.EventsRepository

class GetEventsByPerson(
    private val repository: EventsRepository
) {
    operator fun invoke(personId: Long): Flow<List<Event>> = flow {
        repository.events.collect { events ->
            emit(events.filter { event -> event.personId == personId }.sortedBy { it.label })
        }
    }
}