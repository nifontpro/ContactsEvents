package ru.nifontbus.contactsevents.presentation.persons

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.nifontbus.contactsevents.domain.use_cases.persons.PersonsUseCases
import ru.nifontbus.contactsevents.domain.use_cases.settings.SettingsUseCases
import javax.inject.Inject

@HiltViewModel
class PersonsViewModel @Inject constructor(
    personsUseCases: PersonsUseCases,
    settingsUseCases: SettingsUseCases
) : ViewModel() {

    var searchState = MutableStateFlow("")

//    var searchState = mutableStateOf("")
    private val currentGroup = settingsUseCases.getCurrentGroup()

    val persons = /*if (searchState.value.isEmpty()) personsUseCases.getPersonsFromGroup(currentGroup)
    else*/ personsUseCases.getPersonsFilteredFromGroup(currentGroup, searchState)

}