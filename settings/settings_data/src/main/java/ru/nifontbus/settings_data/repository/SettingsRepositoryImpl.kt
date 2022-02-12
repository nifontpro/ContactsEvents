package ru.nifontbus.settings_data.repository

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.nifontbus.groups_domain.model.PersonsGroup
import ru.nifontbus.settings_domain.repository.SettingsRepository

class SettingsRepositoryImpl(context: Context) : SettingsRepository {

    private val _currentGroup: MutableStateFlow<PersonsGroup?> = MutableStateFlow(null)
    override val currentGroup = _currentGroup.asStateFlow()

    private val _reposeFeatures: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override val reposeFeatures = _reposeFeatures.asStateFlow()

    private val _add40Day: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override val add40Day = _add40Day.asStateFlow()

    private val sharedPreference =
        context.getSharedPreferences(SHARED_SETTING, Context.MODE_PRIVATE)

    init {
        loadCurrentGroupId()
        loadSettings()
    }

    override fun setCurrentGroup(group: PersonsGroup?) {
        _currentGroup.value = group
        saveCurrentGroup(group)
    }

    private fun saveCurrentGroup(group: PersonsGroup?) =
        CoroutineScope(Dispatchers.Main).launch {
            val editor = sharedPreference.edit()
            editor.putLong(GROUP_ID, group?.id ?: -1L)
            editor.putString(GROUP_NAME, group?.title)
            editor.apply()
        }

    private fun loadCurrentGroupId() {
        val id: Long = sharedPreference.getLong(GROUP_ID, -1L)
        val name: String? = sharedPreference.getString(GROUP_NAME, null)
        if (id != -1L && name != null) {
            _currentGroup.value = PersonsGroup(title = name, id = id)
        } else {
            _currentGroup.value = null
        }
    }

    private fun loadSettings() {
        _reposeFeatures.value = sharedPreference.getBoolean(IS_REPOSE_FUN, false)
        _add40Day.value = sharedPreference.getBoolean(IS_40_DAY, false)
    }

    override fun saveReposeState(value: Boolean) {
        CoroutineScope(Dispatchers.Main).launch {
            _reposeFeatures.value = value
            val editor = sharedPreference.edit()
            editor.putBoolean(IS_REPOSE_FUN, value)
            editor.apply()
        }
    }

    override fun saveAdd40DayState(value: Boolean) {
        CoroutineScope(Dispatchers.Main).launch {
            _add40Day.value = value
            val editor = sharedPreference.edit()
            editor.putBoolean(IS_40_DAY, value)
            editor.apply()
        }
    }

    companion object {
        const val SHARED_SETTING = "Current_setting"
        private const val GROUP_ID = "group_id"
        private const val GROUP_NAME = "group_name"

        private const val IS_REPOSE_FUN = "repose_features"
        private const val IS_40_DAY = "add_40_day"
    }
}