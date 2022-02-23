package ru.nifontbus.persons_presenter

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import ru.nifontbus.core_ui.component.BottomNavItem
import ru.nifontbus.groups_domain.use_cases.GroupsUseCases
import ru.nifontbus.persons_domain.model.Person
import ru.nifontbus.persons_domain.use_cases.PersonsUseCases
import ru.nifontbus.settings_domain.model.MainEvent
import ru.nifontbus.settings_domain.use_cases.MetadataUseCases
import javax.inject.Inject

@HiltViewModel
class PersonsViewModel @Inject constructor(
    private val personsUseCases: PersonsUseCases,
    private val metadataUseCases: MetadataUseCases,
    groupsUseCases: GroupsUseCases
) : ViewModel() {

    var searchState = mutableStateOf("")
    private val currentGroup = groupsUseCases.getCurrentGroup()

    private val _persons = mutableStateOf(emptyList<Person>())
    val persons: State<List<Person>> = _persons

    private var job: Job = Job()

    init {
        syncPersonsSubscribe()
        updatePerson()
    }

    private fun syncPersonsSubscribe() = viewModelScope.launch {
        metadataUseCases.subscribeEvent(MainEvent.SyncAll) {
            job.cancelAndJoin()
            job = CoroutineScope(Dispatchers.Default).launch {
                personsUseCases.syncPersons()
            }
        }
    }

    fun updatePerson() = viewModelScope.launch {
        personsUseCases.getPersonsFilteredFromGroup(
            group = currentGroup,
            filter = searchState.value
        ).collectLatest {
            _persons.value = it
            BottomNavItem.PersonItem.badgeCount.value = it.size
        }
    }
}