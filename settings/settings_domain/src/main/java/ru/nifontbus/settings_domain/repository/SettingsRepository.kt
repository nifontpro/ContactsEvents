package ru.nifontbus.settings_domain.repository

import kotlinx.coroutines.flow.StateFlow
import ru.nifontbus.groups_domain.model.PersonsGroup

interface SettingsRepository {

    val showNotification: StateFlow<Boolean>

    val reposeFeatures: StateFlow<Boolean>

    val add40Day: StateFlow<Boolean>

    fun saveReposeState(value: Boolean)

    fun saveAdd40DayState(value: Boolean)

    fun saveNotificationState(value: Boolean)
}