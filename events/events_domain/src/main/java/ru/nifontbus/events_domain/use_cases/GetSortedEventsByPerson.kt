package ru.nifontbus.events_domain.use_cases

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.nifontbus.core.util.asString
import ru.nifontbus.core.util.toMonthAndDay
import ru.nifontbus.events_domain.model.Event
import ru.nifontbus.events_domain.repository.EventsRepository
import ru.nifontbus.events_domain.use_cases.GetSortedEvents.Companion.eventsAfterNow
import ru.nifontbus.events_domain.use_cases.GetSortedEvents.Companion.eventsBeforeNow
import java.time.LocalDate

class GetSortedEventsByPerson(
    private val repository: EventsRepository
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