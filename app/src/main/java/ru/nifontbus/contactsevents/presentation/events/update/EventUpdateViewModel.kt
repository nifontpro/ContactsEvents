package ru.nifontbus.contactsevents.presentation.events.update

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
import ru.nifontbus.contactsevents.domain.data.*
import ru.nifontbus.contactsevents.domain.use_cases.events.EventsUseCases
import ru.nifontbus.contactsevents.domain.use_cases.persons.PersonsUseCases
import javax.inject.Inject

@HiltViewModel
class EventUpdateViewModel @Inject constructor(
    private val eventsUseCases: EventsUseCases,
    private val personsUseCases: PersonsUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _person = mutableStateOf(Person())
    val person: State<Person> = _person

    private val _oldEvent = mutableStateOf(Event())
    private val oldEvent: State<Event> = _oldEvent

    private val _eventLabel = mutableStateOf("")
    val eventLabel: State<String> = _eventLabel

    val eventType = mutableStateOf(EventType.CUSTOM)

    val date = mutableStateOf("")

    private val _action = MutableSharedFlow<String>()
    val action: SharedFlow<String> = _action.asSharedFlow()

    init {
        savedStateHandle.get<Long>("person_id")?.let { it ->
            personsUseCases.getPersonById(it)?.let { findPerson ->
                _person.value = findPerson
            }
        }

        savedStateHandle.get<Long>("event_id")?.let { it ->
            eventsUseCases.getEventById(it)?.let { findEvent ->
                _oldEvent.value = findEvent
                _eventLabel.value = findEvent.label
                eventType.value = findEvent.type
                date.value = findEvent.date
            }
        }
    }

    fun addEvent() = viewModelScope.launch {
        when (val result =
            eventsUseCases.addEvent(
                Event(eventLabel.value, date.value, eventType.value, person.value.id)
            )) {
            is Resource.Success -> {
                _eventLabel.value = ""
                eventType.value = EventType.CUSTOM
                sendMessage(result.message)
            }
            is Resource.Error -> sendMessage(result.message)
        }
    }

    fun updateEvent() = viewModelScope.launch {
        val newEvent =
            Event(eventLabel.value, date.value, eventType.value, person.value.id, oldEvent.value.id)
        when (val result =
            eventsUseCases.updateEvent(newEvent, oldEvent.value)
        ) {
            is Resource.Success -> {
                sendMessage(result.message)
                _oldEvent.value = newEvent
            }
            is Resource.Error -> sendMessage(result.message)
        }
    }

    private suspend fun sendMessage(msg: String) {
        _action.emit(msg)
    }

    fun setEventLabel(name: String) {
        _eventLabel.value = name
    }

/*    fun setCurrentDate(newDate: String) {
        date.value = newDate
    }*/

    fun isEnabledSave(): Boolean = eventLabel.value.isNotEmpty() && date.value.isNotEmpty()

    fun isEnabledEdit(): Boolean = eventType.value == EventType.CUSTOM

    fun isEnabledUpdate(): Boolean = eventLabel.value.isNotEmpty() && date.value.isNotEmpty() &&

            (oldEvent.value.type == EventType.CUSTOM &&
                    !(oldEvent.value.label == eventLabel.value &&
                    oldEvent.value.type == eventType.value &&
                    oldEvent.value.date == date.value) ||

                    oldEvent.value.type != EventType.CUSTOM &&
                    !(oldEvent.value.type == eventType.value &&
                            oldEvent.value.date == date.value)
                    )
}