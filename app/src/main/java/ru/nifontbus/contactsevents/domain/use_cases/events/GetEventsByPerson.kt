package ru.nifontbus.contactsevents.domain.use_cases.events

import kotlinx.coroutines.flow.map
import ru.nifontbus.contactsevents.domain.repository.ContactsRepository

class GetEventsByPerson(
    private val repository: ContactsRepository
) {
    operator fun invoke(personId: Long) = repository.events.map { events ->
        events.filter { event -> event.personId == personId }.sortedBy { it.label }
    }
}