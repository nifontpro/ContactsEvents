package ru.nifontbus.events_presenter.update

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
import ru.nifontbus.core.domain.model.Resource
import ru.nifontbus.core.util.isShortDate
import ru.nifontbus.core.util.toMonthAndDay
import ru.nifontbus.core.util.toShortDate
import ru.nifontbus.core_ui.Arg
import ru.nifontbus.events_domain.model.Event
import ru.nifontbus.events_domain.model.EventType
import ru.nifontbus.events_domain.use_cases.EventsUseCases
import ru.nifontbus.persons_domain.model.Person
import ru.nifontbus.persons_domain.use_cases.PersonsUseCases
import java.time.LocalDate
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

    val isNoYear = mutableStateOf(false)

    private val _action = MutableSharedFlow<String>()
    val action: SharedFlow<String> = _action.asSharedFlow()

    init {
        savedStateHandle.get<Long>(Arg.personId)?.let { it ->
            personsUseCases.getPersonById(it)?.let { findPerson ->
                _person.value = findPerson
            }
        }

        savedStateHandle.get<Long>(Arg.eventId)?.let { it ->
            eventsUseCases.getEventById(it)?.let { findEvent ->
                _oldEvent.value = findEvent
                _eventLabel.value = findEvent.label
                eventType.value = findEvent.type

                if (!findEvent.date.isShortDate()) {
                    date.value = findEvent.date
                    isNoYear.value = false
                } else {
                    val now = LocalDate.now()
                    val year = now.year
                    date.value = "$year-${findEvent.date.toMonthAndDay()}"
                    isNoYear.value = true
                }
            }
        }
    }

    fun addEvent() = viewModelScope.launch {
        when (val result =
            eventsUseCases.addEvent(
                Event(eventLabel.value, getRealDate(), eventType.value, person.value.id)
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
            Event(
                eventLabel.value,
                getRealDate(),
                eventType.value,
                person.value.id,
                oldEvent.value.id
            )
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

    private fun getRealDate() = if (isNoYear.value) date.value.toShortDate() else date.value

    private suspend fun sendMessage(msg: String) {
        _action.emit(msg)
    }

    fun setEventLabel(name: String) {
        _eventLabel.value = name
    }

    fun isEnabledSave(): Boolean = eventLabel.value.isNotEmpty() && date.value.isNotEmpty()

    fun isEnabledEdit(): Boolean = eventType.value == EventType.CUSTOM

    fun isEnabledUpdate(): Boolean = eventLabel.value.isNotEmpty() && date.value.isNotEmpty() &&

            (oldEvent.value.type == EventType.CUSTOM &&
                    !(oldEvent.value.label == eventLabel.value &&
                            oldEvent.value.type == eventType.value &&
                            oldEvent.value.date == getRealDate()) ||

                    oldEvent.value.type != EventType.CUSTOM &&
                    !(oldEvent.value.type == eventType.value &&
                            oldEvent.value.date == getRealDate())
                    )
}