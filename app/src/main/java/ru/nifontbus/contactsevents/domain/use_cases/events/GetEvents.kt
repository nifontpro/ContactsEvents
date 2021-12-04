package ru.nifontbus.contactsevents.domain.use_cases.events

import kotlinx.coroutines.flow.StateFlow
import ru.nifontbus.contactsevents.domain.data.Event
import ru.nifontbus.contactsevents.domain.repository.ContactsRepository

class GetEvents(
    private val repository: ContactsRepository
) {
    operator fun invoke(): StateFlow<List<Event>> = repository.events
}