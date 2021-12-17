package ru.nifontbus.contactsevents.domain.use_cases.events

data class EventsUseCases constructor(
    val addEvent: AddEvent,
    val getEvents: GetEvents,
    val getSortedEvents: GetSortedEvents,
    val getEventsByPerson: GetEventsByPerson,
    val getSortedEventsByPerson: GetSortedEventsByPerson,
    val deleteEvent: DeleteEvent
)
