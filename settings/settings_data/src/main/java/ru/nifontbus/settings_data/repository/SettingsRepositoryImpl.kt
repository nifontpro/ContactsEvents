package ru.nifontbus.settings_data.repository

import android.content.SharedPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.nifontbus.groups_domain.model.PersonsGroup
import ru.nifontbus.settings_domain.repository.SettingsRepository

class SettingsRepositoryImpl(
    private val sharedPreferences: SharedPreferences
) : SettingsRepository {

    private val _reposeFeatures: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override val reposeFeatures = _reposeFeatures.asStateFlow()

    private val _add40Day: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override val add40Day = _add40Day.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        _reposeFeatures.value = sharedPreferences.getBoolean(IS_REPOSE_FUN, false)
        _add40Day.value = sharedPreferences.getBoolean(IS_40_DAY, false)
    }

    override fun saveReposeState(value: Boolean) {
        CoroutineScope(Dispatchers.Main).launch {
            _reposeFeatures.value = value
            val editor = sharedPreferences.edit()
            editor.putBoolean(IS_REPOSE_FUN, value)
            editor.apply()
        }
    }

    override fun saveAdd40DayState(value: Boolean) {
        CoroutineScope(Dispatchers.Main).launch {
            _add40Day.value = value
            val editor = sharedPreferences.edit()
            editor.putBoolean(IS_40_DAY, value)
            editor.apply()
        }
    }

    companion object {
        private const val IS_REPOSE_FUN = "repose_features"
        private const val IS_40_DAY = "add_40_day"
    }
}