package ru.nifontbus.persons_presenter.info

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.nifontbus.core.domain.model.Resource
import ru.nifontbus.core_ui.Argument
import ru.nifontbus.events_domain.model.Event
import ru.nifontbus.events_domain.use_cases.EventsUseCases
import ru.nifontbus.groups_domain.use_cases.GroupsUseCases
import ru.nifontbus.persons_domain.model.Person
import ru.nifontbus.persons_domain.model.PersonInfo
import ru.nifontbus.persons_domain.use_cases.PersonsUseCases
import ru.nifontbus.settings_domain.use_cases.MetadataUseCases
import javax.inject.Inject

@HiltViewModel
class PersonInfoViewModel @Inject constructor(
    private val personsUseCases: PersonsUseCases,
    private val groupsUseCases: GroupsUseCases,
    private val eventsUseCases: EventsUseCases,
    private val metadataUseCases: MetadataUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _person = mutableStateOf(Person())
    val person: State<Person> = _person

    private val _personInfo = mutableStateOf(PersonInfo())
    val personInfo: State<PersonInfo> = _personInfo

    val displayPhoto: MutableState<ImageBitmap?> = mutableStateOf(null)

    val personEvents by lazy {
        eventsUseCases.getSortedEventsByPerson(person.value.id)
    }

    private val _action = MutableSharedFlow<String>()
    val action: SharedFlow<String> = _action.asSharedFlow()

    init {
        viewModelScope.launch {
            savedStateHandle.get<Long>(Argument.personId)?.let { it ->
                personsUseCases.getPersonById(it)?.let { findPerson ->
                    _person.value = findPerson
                }
                _personInfo.value = getPersonInfo(it)
            }
            displayPhoto.value = getDisplayPhoto(person.value.id)
        }
    }

    fun getGroupById(id: Long) = groupsUseCases.getGroupById(id)

    private suspend fun getPersonInfo(id: Long) = personsUseCases.getPersonInfo(id)

    private suspend fun getDisplayPhoto(id: Long) = personsUseCases.getDisplayPhoto(id)

    fun deleteEvent(event: Event) = viewModelScope.launch {
        metadataUseCases.resetSyncTime()
        when (val result = eventsUseCases.deleteEvent(event)) {
            is Resource.Success -> {
                sendMessage(result.message)
            }
            is Resource.Error -> {
                sendMessage(result.message)
            }
        }
    }

    private suspend fun sendMessage(msg: String) {
        _action.emit(msg)
    }
}