package ru.nifontbus.contactsevents.domain.use_cases.events

import ru.nifontbus.contactsevents.domain.data.Event
import ru.nifontbus.contactsevents.domain.repository.ContactsRepository

class AddEvent(
    private val repository: ContactsRepository
) {
    operator fun invoke(event: Event) {
        repository.addEvent(event)
    }
}