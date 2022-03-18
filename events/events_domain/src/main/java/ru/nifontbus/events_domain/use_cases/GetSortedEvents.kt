package ru.nifontbus.events_domain.use_cases

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.nifontbus.core.util.asString
import ru.nifontbus.core.util.getLocalizedDate
import ru.nifontbus.core.util.toLocalDate
import ru.nifontbus.core.util.toMonthAndDay
import ru.nifontbus.events_domain.R
import ru.nifontbus.events_domain.model.Event
import ru.nifontbus.events_domain.model.EventType
import ru.nifontbus.events_domain.repository.EventsRepository
import ru.nifontbus.settings_domain.repository.SettingsRepository
import java.time.LocalDate

const val MIN_DATE = "01-01"

class GetSortedEvents(
    private val repository: EventsRepository,
    private val settingsRepo: SettingsRepository,
    private val context: Context
) {

    operator fun invoke(): Flow<List<Event>> = flow {

        repository.events.collect {
            val events = (if (settingsRepo.reposeFeatures.value && settingsRepo.add40Day.value) {
                add40Day(it)
            } else it).filter { event ->
                settingsRepo.reposeFeatures.value ||
                        (!settingsRepo.reposeFeatures.value && event.type != EventType.NEW_LIFE_DAY)
            }
            val now = LocalDate.now().asString().toMonthAndDay()
            coroutineScope {
                val eventsBeforeNow = async { eventsBeforeNow(events, now) }
                val eventsAfterNow = async { eventsAfterNow(events, now) }
                emit(eventsAfterNow.await() + eventsBeforeNow.await())
            }
        }
    }.flowOn(Dispatchers.Default)

    private fun add40Day(events: List<Event>): List<Event> {
        val mutableEvents = events.toMutableList()
        events.forEach { event ->
            if (event.type == EventType.NEW_LIFE_DAY) {
                try {
                    val day40date = event.date.toLocalDate().plusDays(39)
                    if (day40date >= LocalDate.now()) {
                        mutableEvents.add(
                            Event(
                                label = context.getString(
                                    R.string.s40Days,
                                    event.date.getLocalizedDate()
                                ),
                                date = day40date.asString(),
                                type = EventType.CUSTOM,
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

    companion object {
        fun eventsBeforeNow(events: List<Event>, now: String): List<Event> {
            val listBefore = events.filter { event ->
                val current = event.date.toMonthAndDay()
                current < now && current > MIN_DATE
            }.sortedBy { event ->
                event.date.toMonthAndDay()
            }
            return listBefore
        }

        fun eventsAfterNow(events: List<Event>, now: String): List<Event> {
            val listAfter = events.filter { event ->
                event.date.toMonthAndDay() >= now
            }.sortedBy { event ->
                event.date.toMonthAndDay()
            }
            return listAfter
        }
    }
}