package ru.nifontbus.contactsevents.presentation.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.nifontbus.contactsevents.domain.use_cases.persons.PersonsUseCases
import ru.nifontbus.contactsevents.presentation.navigation.BottomNavItem
import ru.nifontbus.core.domain.model.Person
import ru.nifontbus.events_domain.use_cases.EventsUseCases
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
    suspend fun getPhotoById(id: Long) = personsUseCases.getPhotoById(id)

    init {
        viewModelScope.launch {
            events.collect {
                BottomNavItem.EventItem.badgeCount.value = it.size
            }
        }
    }

//    fun getGroupsById(id: String) = groupsUseCases.getGroupById(id)
}