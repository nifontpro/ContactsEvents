package ru.nifontbus.contactsevents.domain.use_cases.events

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.nifontbus.contactsevents.domain.data.Const
import ru.nifontbus.contactsevents.domain.data.Event
import ru.nifontbus.contactsevents.domain.repository.ContactsRepository
import ru.nifontbus.contactsevents.domain.utils.asString
import ru.nifontbus.contactsevents.domain.utils.toLocalDate
import ru.nifontbus.contactsevents.domain.utils.toMonthAndDay
import java.time.LocalDate

const val MIN_DATE = "01-01"

class GetSortedEvents(
    private val repository: ContactsRepository
) {

    operator fun invoke(): Flow<List<Event>> = flow {

        repository.events.collect {
            val events = add40Day(it)
            val now = LocalDate.now().asString().toMonthAndDay()
            coroutineScope {
                val eventsBeforeNow = async { eventsBeforeNow(events, now) }
                val eventsAfterNow = async { eventsAfterNow(events, now) }
                emit(eventsAfterNow.await() + eventsBeforeNow.await())
            }
        }
    }.flowOn(Dispatchers.Default)

    private fun eventsBeforeNow(events: List<Event>, now: String): List<Event> {
        val listBefore = events.filter { event ->
            val current = event.date.toMonthAndDay()
            current < now && current > MIN_DATE
        }.sortedBy { event ->
            event.date.toMonthAndDay()
        }
        return listBefore
    }

    private fun eventsAfterNow(events: List<Event>, now: String): List<Event> {
        val listAfter = events.filter { event ->
            event.date.toMonthAndDay() >= now
        }.sortedBy { event ->
            event.date.toMonthAndDay()
        }
        return listAfter
    }

    private fun add40Day(events: List<Event>): List<Event> {
        val mutableEvents = events.toMutableList()
        events.forEach { event ->
            if (event.type == Const.TYPE_DAY_OF_DEATH) {
                try {
                    val day40date = event.date.toLocalDate().plusDays(39)
                    if (day40date > LocalDate.now()) {
                        mutableEvents.add(
                            Event(
                                "40 day of death /${event.date}/",
                                day40date.asString(),
                                personId = event.personId,
                            )
                        )
                    }
                } catch (e: Exception) {
                }
            }
        }
        return mutableEvents
    }
}