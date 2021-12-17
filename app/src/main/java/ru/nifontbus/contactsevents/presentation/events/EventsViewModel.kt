package ru.nifontbus.contactsevents.presentation.events

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import ru.nifontbus.contactsevents.domain.data.Person
import ru.nifontbus.contactsevents.domain.use_cases.events.EventsUseCases
import ru.nifontbus.contactsevents.domain.use_cases.persons.PersonsUseCases
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(
    eventsUseCases: EventsUseCases,
    private val personsUseCases: PersonsUseCases,
//    private val groupsUseCases: GroupsUseCases
) : ViewModel() {

//    val events = eventsUseCases.getEvents()
    val events = eventsUseCases.getSortedEvents()

    fun getPersonByIdFlow(id: Long): Flow<Person?> = personsUseCases.getPersonByIdFlow(id)
    fun getPhotoByUri(photoUri: String) = personsUseCases.getPhotoByUri(photoUri)
    fun getPhotoById(id: Long) = personsUseCases.getPhotoById(id)

//    fun getGroupsById(id: String) = groupsUseCases.getGroupById(id)
}