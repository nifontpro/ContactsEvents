package ru.nifontbus.events_domain.use_cases

data class EventsUseCases constructor(
    val addEvent: AddEvent,
    val getEvents: GetEvents,
    val getSortedEvents: GetSortedEvents,
    val getEventsByPerson: GetEventsByPerson,
    val getSortedEventsByPerson: GetSortedEventsByPerson,
    val deleteEvent: DeleteEvent,
    val getEventById: GetEventById,
    val updateEvent: UpdateEvent,
    val syncEvents: SyncEvents
)
