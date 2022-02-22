package ru.nifontbus.groups_data.repository

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.provider.ContactsContract
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.nifontbus.groups_domain.model.PersonsGroup
import ru.nifontbus.groups_domain.repository.GroupsRepository

class GroupsRepositoryImpl(
    private val context: Context,
    private val sharedPreferences: SharedPreferences
) : GroupsRepository {

    private val _groups = MutableStateFlow<List<PersonsGroup>>(emptyList())
    override val groups = _groups.asStateFlow()

    private val _currentGroup: MutableStateFlow<PersonsGroup?> = MutableStateFlow(null)
    override val currentGroup = _currentGroup.asStateFlow()

    init {
        groupsUpdate()
    }

    private fun groupsUpdate() = CoroutineScope(Dispatchers.Default).launch {
        val uri: Uri = ContactsContract.Groups.CONTENT_URI
        val projection = arrayOf(
            ContactsContract.Groups.TITLE,
            ContactsContract.Groups._ID,
            ContactsContract.Groups.ACCOUNT_NAME,
        )

        val sortOrder = ContactsContract.Groups.ACCOUNT_NAME
        val cursor = context.contentResolver.query(
            uri, projection, null, null, sortOrder
        )

        cursor?.let {

            val idIdx = it.getColumnIndex(ContactsContract.Groups._ID)
            val titleIdx = it.getColumnIndex(ContactsContract.Groups.TITLE)
            val accountIdx = it.getColumnIndex(ContactsContract.Groups.ACCOUNT_NAME)

            val groupsList = mutableListOf<PersonsGroup>()

            while (it.moveToNext()) {
                val id = it.getLong(idIdx)
                val title = it.getString(titleIdx) ?: "?"
                val account = it.getString(accountIdx) ?: ""
                val newGroup = PersonsGroup(title, account, id)
                groupsList.add(newGroup)
            }
            it.close()
            _groups.value = groupsList + listOf(PersonsGroup())
        }
        loadCurrentGroup()
    }

    override fun setCurrentGroup(group: PersonsGroup?) {
        _currentGroup.value = group
        saveCurrentGroup(group)
    }

    private fun saveCurrentGroup(group: PersonsGroup?) =
        CoroutineScope(Dispatchers.Main).launch {
            val editor = sharedPreferences.edit()
            editor.putLong(GROUP_ID, group?.id ?: -1L)
            editor.putString(GROUP_NAME, group?.title)
            editor.apply()
        }

    private fun loadCurrentGroup() {
        val id: Long = sharedPreferences.getLong(GROUP_ID, -1L)
        val name: String? = sharedPreferences.getString(GROUP_NAME, null)
        if (id != -1L && name != null) {
            _currentGroup.value = PersonsGroup(title = name, id = id)
        } else {
            _currentGroup.value = if (groups.value.isNotEmpty()) groups.value[0] else null
        }
    }

    companion object {
        private const val GROUP_ID = "group_id"
        private const val GROUP_NAME = "group_name"
    }
}

/*
fun String.toIntDefault(default: Int) =
    try {
        this.toInt()
    } catch (e: Exception) {
        default
    }*/
