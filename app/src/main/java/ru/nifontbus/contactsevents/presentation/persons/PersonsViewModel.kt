package ru.nifontbus.contactsevents.presentation.persons

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.nifontbus.contactsevents.domain.use_cases.persons.PersonsUseCases
import ru.nifontbus.contactsevents.domain.use_cases.settings.SettingsUseCases
import javax.inject.Inject

@HiltViewModel
class PersonsViewModel @Inject constructor(
    personsUseCases: PersonsUseCases,
    settingsUseCases: SettingsUseCases
) : ViewModel() {

    private val currentGroup = settingsUseCases.getCurrentGroup()
    val persons = personsUseCases.getPersonsFromGroup(currentGroup)
}