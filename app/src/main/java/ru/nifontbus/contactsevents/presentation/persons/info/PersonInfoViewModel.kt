package ru.nifontbus.contactsevents.presentation.persons.info

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.nifontbus.contactsevents.domain.data.Person
import ru.nifontbus.contactsevents.domain.use_cases.events.EventsUseCases
import ru.nifontbus.contactsevents.domain.use_cases.groups.GroupsUseCases
import ru.nifontbus.contactsevents.domain.use_cases.persons.PersonsUseCases
import javax.inject.Inject

@HiltViewModel
class PersonInfoViewModel @Inject constructor(
    private val personsUseCases: PersonsUseCases,
    private val groupsUseCases: GroupsUseCases,
    private val eventsUseCases: EventsUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // PersonInfoScreen:
    private val _person = mutableStateOf(Person())
    val person: State<Person> = _person

    val personEvents by lazy {
        eventsUseCases.getEventsByPerson(person.value.id)
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
            savedStateHandle.get<Long>("id")?.let { it ->
                personsUseCases.getPersonById(it).collect {
                    it?.let { findPerson ->
                        _person.value = findPerson
                    }
                }
            }
        }
    }

    fun getGroupById(id: Long) = groupsUseCases.getGroupById(id)

    fun getPersonInfo(id: Long) = personsUseCases.getPersonInfo(id)

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

 /*   private suspend fun sendMessage(msg: String) {
        _action.emit(msg)
    }*/
}