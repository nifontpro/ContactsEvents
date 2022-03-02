package ru.nifontbus.events_presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.nifontbus.core_ui.component.BottomNavItem
import ru.nifontbus.events_domain.use_cases.EventsUseCases
import ru.nifontbus.groups_domain.use_cases.GroupsUseCases
import ru.nifontbus.persons_domain.model.Person
import ru.nifontbus.persons_domain.use_cases.PersonsUseCases
import ru.nifontbus.settings_domain.model.MainEvent
import ru.nifontbus.settings_domain.use_cases.MetadataUseCases
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(
    private val eventsUseCases: EventsUseCases,
    private val personsUseCases: PersonsUseCases,
    private val groupsUseCases: GroupsUseCases,
    private val metadataUseCases: MetadataUseCases
) : ViewModel() {

    val events = eventsUseCases.getSortedEvents()

    init {
        viewModelScope.launch {
            syncEventsSubscribe()

            events.collectLatest {
                BottomNavItem.EventItem.badgeCount.value = it.size
            }
        }
    }

    private fun syncEventsSubscribe() = viewModelScope.launch {
        metadataUseCases.getEvent().collectLatest { event ->
            when (event) {
                is MainEvent.SyncAll -> {
                    eventsUseCases.syncEvents()
                }
                is MainEvent.SilentSyncAll -> {
                    eventsUseCases.silentSync()
                    personsUseCases.silentSync()
                    groupsUseCases.syncGroups()
                }
            }
        }
    }

    fun getPersonByIdFlow(id: Long): Flow<Person?> = personsUseCases.getPersonByIdFlow(id)

    fun syncAll() = viewModelScope.launch {
        metadataUseCases.sendEvent(MainEvent.SyncAll)
    }
}