package ru.nifontbus.contactsevents.presentation.persons

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.nifontbus.contactsevents.domain.data.Person
import ru.nifontbus.contactsevents.domain.use_cases.persons.PersonsUseCases
import ru.nifontbus.contactsevents.domain.use_cases.settings.SettingsUseCases
import javax.inject.Inject

@HiltViewModel
class PersonsViewModel @Inject constructor(
    private val personsUseCases: PersonsUseCases,
    settingsUseCases: SettingsUseCases
) : ViewModel() {

    var searchState = mutableStateOf("")
    private val currentGroup = settingsUseCases.getCurrentGroup()

    private val _persons = mutableStateOf(emptyList<Person>())
    val persons: State<List<Person>> = _persons

    init {
        updatePerson()
    }

    fun updatePerson() = viewModelScope.launch {
        personsUseCases.getPersonsFilteredFromGroup(currentGroup, searchState.value).collect {
            _persons.value = it
        }
    }
}