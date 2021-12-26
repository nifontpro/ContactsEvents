package ru.nifontbus.contactsevents.domain.use_cases.events

import ru.nifontbus.contactsevents.domain.data.Event
import ru.nifontbus.contactsevents.domain.data.Resource
import ru.nifontbus.contactsevents.domain.repository.ContactsRepository

class UpdateEvent(
    private val repository: ContactsRepository
) {
    suspend operator fun invoke(newEvent: Event, oldEvent: Event) =
        repository.updateEvent(newEvent, oldEvent)
}