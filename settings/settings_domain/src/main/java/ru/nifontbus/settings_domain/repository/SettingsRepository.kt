package ru.nifontbus.settings_domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface SettingsRepository {

    val settings: Flow<AppSettings>

    val showNotification: StateFlow<Boolean>

    val reposeFeatures: StateFlow<Boolean>

    val add40Day: StateFlow<Boolean>

    fun saveReposeState(value: Boolean)

    fun saveAdd40DayState(value: Boolean)

    fun saveNotificationState(value: Boolean)
}