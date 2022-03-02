package ru.nifontbus.events_domain.repository

import kotlinx.coroutines.flow.StateFlow
import ru.nifontbus.core.domain.model.Resource
import ru.nifontbus.events_domain.model.Event

interface EventsRepository {

    val events: StateFlow<List<Event>>

    suspend fun addEvent(event: Event): Resource<Unit>

    suspend fun updateEvent(newEvent: Event, oldEvent: Event): Resource<Unit>

    suspend fun deleteEvent(event: Event): Resource<Unit>

    fun syncEvents()

    fun silentSync()
}