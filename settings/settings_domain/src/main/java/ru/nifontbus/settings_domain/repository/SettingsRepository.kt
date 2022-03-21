package ru.nifontbus.settings_domain.repository

import kotlinx.coroutines.flow.StateFlow

interface SettingsRepository {

    val settings: StateFlow<AppSettings>

    fun saveReposeState(value: Boolean)

    fun saveAdd40DayState(value: Boolean)

    fun saveNotificationState(value: Boolean)

}