package ru.nifontbus.contactsevents.presentation.events.new_event

import android.provider.ContactsContract
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.nifontbus.contactsevents.domain.data.Event
import ru.nifontbus.contactsevents.domain.data.Person
import ru.nifontbus.contactsevents.domain.data.Resource
import ru.nifontbus.contactsevents.domain.use_cases.events.EventsUseCases
import ru.nifontbus.contactsevents.domain.use_cases.persons.PersonsUseCases
import javax.inject.Inject

@HiltViewModel
class NewEventViewModel @Inject constructor(
    private val eventsUseCases: EventsUseCases,
    private val personsUseCases: PersonsUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _person = mutableStateOf(Person())
    val person: State<Person> = _person

    private val _eventName = mutableStateOf("")
    val eventName: State<String> = _eventName
    private var eventType: Int = ContactsContract.CommonDataKinds.Event.TYPE_CUSTOM

    val date = mutableStateOf("")
//    val date: State<String> = _date

    private val _action = MutableSharedFlow<String>()
    val action: SharedFlow<String> = _action.asSharedFlow()

    init {
        savedStateHandle.get<Long>("id")?.let { it ->
            personsUseCases.getPersonById(it)?.let { findPerson ->
                _person.value = findPerson
            }
        }
    }

    fun addEvent() = viewModelScope.launch {
        when (val result =
            eventsUseCases.addEvent(
                Event(eventName.value, date.value, eventType, person.value.id)
            )) {
            is Resource.Success -> {
                _eventName.value = ""
                eventType = ContactsContract.CommonDataKinds.Event.TYPE_CUSTOM

                sendMessage(result.message)
            }
            is Resource.Error -> sendMessage(result.message)
        }
    }

    private suspend fun sendMessage(msg: String) {
        _action.emit(msg)
    }

    fun setEventName(name: String) {
        _eventName.value = name
    }

/*    fun setCurrentDate(newDate: String) {
        date.value = newDate
    }*/

/*    fun setSelectedTemplate(template: Template) {
        eventType = template.type
        _eventName.value = template.name
    }*/

    fun isEnabledSave(): Boolean = eventName.value.isNotEmpty() && date.value.isNotEmpty()
    fun isEnabledEdit(): Boolean = eventType == ContactsContract.CommonDataKinds.Event.TYPE_CUSTOM
}