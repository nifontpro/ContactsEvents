package ru.nifontbus.contactsevents.domain.use_cases.events

import kotlinx.coroutines.ExperimentalCoroutinesApi

data class EventsUseCases @ExperimentalCoroutinesApi constructor(
//    val addEvent: AddEvent,
    val getEvents: GetEvents,
    val getSortedEvents: GetSortedEvents,
    val getEventsByPerson: GetEventsByPerson,
//    val deleteEvent: DeleteEvent
)
