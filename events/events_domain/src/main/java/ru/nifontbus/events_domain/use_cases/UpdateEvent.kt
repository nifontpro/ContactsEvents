package ru.nifontbus.events_domain.use_cases

import ru.nifontbus.events_domain.model.Event
import ru.nifontbus.events_domain.repository.EventsRepository

class UpdateEvent(
    private val repository: EventsRepository
) {
    suspend operator fun invoke(newEvent: Event, oldEvent: Event) =
        repository.updateEvent(newEvent, oldEvent)
}