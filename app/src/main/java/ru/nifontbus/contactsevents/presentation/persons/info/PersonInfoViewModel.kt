package ru.nifontbus.contactsevents.presentation.persons.info

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.nifontbus.contactsevents.domain.use_cases.groups.GroupsUseCases
import ru.nifontbus.core.domain.model.Resource
import ru.nifontbus.events_domain.model.Event
import ru.nifontbus.events_domain.use_cases.EventsUseCases
import ru.nifontbus.persons_domain.model.Person
import ru.nifontbus.persons_domain.model.PersonInfo
import ru.nifontbus.persons_domain.use_cases.PersonsUseCases
import javax.inject.Inject

@HiltViewModel
class PersonInfoViewModel @Inject constructor(
    private val personsUseCases: PersonsUseCases,
    private val groupsUseCases: GroupsUseCases,
    private val eventsUseCases: EventsUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _person = mutableStateOf(Person())
    val person: State<Person> = _person

    private val _personInfo = mutableStateOf(PersonInfo())
    val personInfo: State<PersonInfo> = _personInfo

    var displayPhoto: ImageBitmap? = null

    val personEvents by lazy {
        eventsUseCases.getSortedEventsByPerson(person.value.id)
    }

/*    // PersonEditScreen:
    var firstName = mutableStateOf("")
    var lastName = mutableStateOf("")
    var family = mutableStateOf("")*/

    private val _action = MutableSharedFlow<String>()
    val action: SharedFlow<String> = _action.asSharedFlow()

//    val deleteBoxInfo = mutableStateOf(false)

/*    private val _deleteListMessage = mutableStateOf<List<String>>(emptyList())
    val deleteListMessage: State<List<String>> = _deleteListMessage*/

    init {
        viewModelScope.launch {
            savedStateHandle.get<Long>("person_id")?.let { it ->
                personsUseCases.getPersonById(it)?.let { findPerson ->
                    _person.value = findPerson
                }
                _personInfo.value = getPersonInfo(it)
            }
            displayPhoto = getDisplayPhoto(person.value.id)
        }
    }

    fun getGroupById(id: Long) = groupsUseCases.getGroupById(id)

    private suspend fun getPersonInfo(id: Long) = personsUseCases.getPersonInfo(id)

    suspend fun getPhotoById(id: Long) = personsUseCases.getPhotoById(id)

    private suspend fun getDisplayPhoto(id: Long) = personsUseCases.getDisplayPhoto(id)

    fun deleteEvent(event: Event) = viewModelScope.launch {
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

/*    fun deleteEvent(id: String) = viewModelScope.launch {
        when (val result = eventsUseCases.deleteEvent(id)) {
            is Resource.Success -> {
                sendMessage(result.message)
            }
            is Resource.Error -> sendMessage(result.message)
        }
    }*/

/*    fun deletePerson() = viewModelScope.launch {
        deleteBoxInfo.value = true
        personsUseCases.deletePersonWithEvents(person.value).collect { message ->
            deleteMessage(message)
        }
    }*/

/*    private fun deleteMessage(message: String) {
        val newList = deleteListMessage.value.toMutableList()
        newList.add(message)
        _deleteListMessage.value = newList
    }*/

/*    private fun updatePersonState() {
        _person.value = person.value.copy(
            firstName = firstName.value,
            lastName = lastName.value,
            family = family.value
        )
    }*/

/*    fun getEditState() {
        firstName.value = person.value.firstName
        lastName.value = person.value.lastName
        family.value = person.value.family
    }*/

/*    fun updatePerson() = viewModelScope.launch {
        when (val result = personsUseCases.updatePerson(
            Person(firstName.value, lastName.value, family.value, id = person.value.id)
        )) {
            is Resource.Success -> {
                sendMessage(result.message)
                updatePersonState()
            }
            is Resource.Error -> sendMessage(result.message)
        }
    }*/

/*
    fun isEnabledUpdatePerson(): Boolean = firstName.value.isNotEmpty() &&
            !((firstName.value == person.value.firstName) &&
                    (lastName.value == person.value.lastName) &&
                    (family.value == person.value.family)
                    )
*/