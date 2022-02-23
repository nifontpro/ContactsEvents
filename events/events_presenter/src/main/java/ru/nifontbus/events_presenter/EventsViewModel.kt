package ru.nifontbus.events_presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.nifontbus.core_ui.component.BottomNavItem
import ru.nifontbus.events_domain.use_cases.EventsUseCases
import ru.nifontbus.persons_domain.model.Person
import ru.nifontbus.persons_domain.use_cases.PersonsUseCases
import ru.nifontbus.settings_domain.model.MainEvent
import ru.nifontbus.settings_domain.use_cases.SettingsUseCases
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(
    eventsUseCases: EventsUseCases,
    private val personsUseCases: PersonsUseCases,
    private val settingsUseCase: SettingsUseCases
) : ViewModel() {

    val events = eventsUseCases.getSortedEvents()

    init {
        viewModelScope.launch {
            events.collect {
                BottomNavItem.EventItem.badgeCount.value = it.size
            }
        }
    }

    fun getPersonByIdFlow(id: Long): Flow<Person?> = personsUseCases.getPersonByIdFlow(id)

    fun syncAll() = viewModelScope.launch {
        settingsUseCase.sendEvent(MainEvent.Sync)
    }
}