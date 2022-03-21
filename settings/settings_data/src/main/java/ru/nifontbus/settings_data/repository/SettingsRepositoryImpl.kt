package ru.nifontbus.settings_data.repository

import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.nifontbus.settings_domain.repository.AppSettings
import ru.nifontbus.settings_domain.repository.SettingsRepository

class SettingsRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
    private val dataStore: DataStore<AppSettings>
) : SettingsRepository {

    override val settings = dataStore.data

    private val _showNotification: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override val showNotification = _showNotification.asStateFlow()

    private val _reposeFeatures: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override val reposeFeatures = _reposeFeatures.asStateFlow()

    private val _add40Day: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override val add40Day = _add40Day.asStateFlow()

    init {
//        loadSettings()
    }

    private fun loadSettings() {
        _reposeFeatures.value = sharedPreferences.getBoolean(IS_REPOSE_FUN, false)
        _add40Day.value = sharedPreferences.getBoolean(IS_40_DAY, false)
        _showNotification.value = sharedPreferences.getBoolean(SHOW_NOTIFICATION, false)
    }

    override fun saveNotificationState(value: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            _showNotification.value = value
            val editor = sharedPreferences.edit()
            editor.putBoolean(SHOW_NOTIFICATION, value)
            editor.apply()

            dataStore.updateData {
                it.copy(showNotifications = value)
            }
        }
    }

    override fun saveReposeState(value: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            _reposeFeatures.value = value
            val editor = sharedPreferences.edit()
            editor.putBoolean(IS_REPOSE_FUN, value)
            editor.apply()

            dataStore.updateData {
                it.copy(reposeFeatures = value)
            }
        }
    }

    override fun saveAdd40DayState(value: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            _add40Day.value = value
            val editor = sharedPreferences.edit()
            editor.putBoolean(IS_40_DAY, value)
            editor.apply()

            dataStore.updateData {
                it.copy(add40Day = value)
            }
        }
    }

    companion object {
        private const val IS_REPOSE_FUN = "repose_features"
        private const val IS_40_DAY = "add_40_day"
        private const val SHOW_NOTIFICATION = "show_notification"
    }
}