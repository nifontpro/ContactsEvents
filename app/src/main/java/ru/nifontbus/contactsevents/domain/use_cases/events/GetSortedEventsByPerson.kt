package ru.nifontbus.contactsevents.domain.use_cases.events

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import ru.nifontbus.contactsevents.domain.data.Event
import ru.nifontbus.contactsevents.domain.repository.ContactsRepository
import ru.nifontbus.contactsevents.domain.use_cases.events.GetSortedEvents.Companion.eventsAfterNow
import ru.nifontbus.contactsevents.domain.use_cases.events.GetSortedEvents.Companion.eventsBeforeNow
import ru.nifontbus.contactsevents.domain.utils.asString
import ru.nifontbus.contactsevents.domain.utils.toMonthAndDay
import java.time.LocalDate

class GetSortedEventsByPerson(
    private val repository: ContactsRepository
) {
    operator fun invoke(personId: Long): Flow<List<Event>> = flow {

        repository.events.collect { allEvents ->
            val events = allEvents.filter { event -> event.personId == personId }
            val now = LocalDate.now().asString().toMonthAndDay()
            coroutineScope {
                val eventsBeforeNow = async { eventsBeforeNow(events, now) }
                val eventsAfterNow = async { eventsAfterNow(events, now) }
                emit(eventsAfterNow.await() + eventsBeforeNow.await())
            }
        }
    }
}