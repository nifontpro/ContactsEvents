package ru.nifontbus.contactsevents.domain.use_cases.events

import ru.nifontbus.contactsevents.domain.repository.ContactsRepository

class GetEventById(
    private val repository: ContactsRepository
) {
    operator fun invoke(id: Long) = repository.events.value.find { it.id == id }
}