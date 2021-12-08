package ru.nifontbus.contactsevents.domain.use_cases.events

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import ru.nifontbus.contactsevents.domain.data.Event
import ru.nifontbus.contactsevents.domain.repository.ContactsRepository

class GetEventsByPerson(
    private val repository: ContactsRepository
) {
    operator fun invoke(personId: Long): Flow<List<Event>> = flow {
        repository.events.collect { events ->
            emit(events.filter { event -> event.personId == personId }.sortedBy { it.label })
        }
    }
}