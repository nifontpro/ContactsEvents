package ru.nifontbus.contactsevents.domain.repository

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.nifontbus.contactsevents.domain.data.ContactsGroup

class SettingsRepo(context: Context) {

    private val _currentGroup: MutableStateFlow<ContactsGroup?> = MutableStateFlow(null)
    val currentGroup = _currentGroup.asStateFlow()

    private val sharedPreference =
        context.getSharedPreferences(SHARED_SETTING, Context.MODE_PRIVATE)

    init {
        loadCurrentGroupId()
    }

    fun setCurrentGroup(group: ContactsGroup?) {
        _currentGroup.value = group
        saveCurrentGroup(group)
    }

    private fun saveCurrentGroup(group: ContactsGroup?) =
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
            _currentGroup.value = ContactsGroup(title = name, id = id)
        } else {
            _currentGroup.value = null
        }
    }

    companion object {
        const val SHARED_SETTING = "Current_setting"
        private const val GROUP_ID = "group_id"
        private const val GROUP_NAME = "group_name"
    }
}