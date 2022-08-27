package ru.nifontbus.settings_data.repository

import androidx.datastore.core.DataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.nifontbus.settings_domain.repository.AppSettings
import ru.nifontbus.settings_domain.repository.SettingsRepository

class SettingsRepositoryImpl(
    private val dataStore: DataStore<AppSettings>
) : SettingsRepository {

    private val _settings = MutableStateFlow(AppSettings())
    override val settings = _settings.asStateFlow()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.data.collectLatest {
                _settings.value = it
            }
        }
    }

    override fun saveNotificationState(value: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.updateData {
                it.copy(showNotifications = value)
            }
        }
    }

    override fun saveReposeState(value: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.updateData {
                it.copy(reposeFeatures = value)
            }
        }
    }

    override fun saveAdd40DayState(value: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.updateData {
                it.copy(add40Day = value)
            }
        }
    }
}