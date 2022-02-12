package ru.nifontbus.settings_domain.repository

import kotlinx.coroutines.flow.StateFlow
import ru.nifontbus.groups_domain.model.PersonsGroup

interface SettingsRepository {

    val currentGroup: StateFlow<PersonsGroup?>

    val reposeFeatures: StateFlow<Boolean>

    val add40Day: StateFlow<Boolean>

    fun setCurrentGroup(group: PersonsGroup?)

    fun saveReposeState(value: Boolean)

    fun saveAdd40DayState(value: Boolean)

}