package ru.nifontbus.contactsevents.domain.repository

import android.content.Context
import android.database.Cursor
import android.database.DatabaseUtils
import android.net.Uri
import android.provider.ContactsContract
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.nifontbus.contactsevents.domain.data.Event
import ru.nifontbus.contactsevents.domain.data.Person
import ru.nifontbus.contactsevents.domain.data.PersonsGroup
import ru.nifontbus.contactsevents.domain.data.person_info.Phone
import ru.nifontbus.contactsevents.domain.data.person_info.PersonInfo


class ContactsRepository(private val context: Context) {

    private val _persons = MutableStateFlow<List<Person>>(emptyList())
    val persons = _persons.asStateFlow()

    private val _groups = MutableStateFlow<List<PersonsGroup>>(emptyList())
    val groups = _groups.asStateFlow()

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events = _events.asStateFlow()

    init {
        personsUpdate()
        groupsUpdate()
        eventsUpdate()
    }

    // +contacts
    // https://stackru.com/questions/32761829/vstavka-novogo-kontakta-programmno-cherez-moe-prilozhenie-bez-ispolzovaniya-intent

    private fun personsUpdate() = CoroutineScope(Dispatchers.Default).launch {
        val uri = ContactsContract.Contacts.CONTENT_URI

        val projection = arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.Contacts.HAS_PHONE_NUMBER,
        )
/*        val where = ContactsContract.Contacts.DISPLAY_NAME + " LIKE ?"
        val selectionArgs = arrayOf("А%")*/
        val sortOrder = ContactsContract.Contacts.DISPLAY_NAME

        val cursor = context.contentResolver.query(
            uri, projection, null, null, sortOrder
        )

//        Log.e("my", DatabaseUtils.dumpCursorToString(cursor))

        cursor?.let {
//            val keyRef = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY)
            val idRef = it.getColumnIndex(ContactsContract.Contacts._ID)
            val displayNameRef = it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
            val hasPhoneNumberRef = it.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)

            while (it.moveToNext()) {
//                val key = it.getString(keyRef) // LookUp Key
                val id = it.getLong(idRef)
                val hasPhoneNumber = it.getInt(hasPhoneNumberRef) == 1
                val displayName = it.getString(displayNameRef) ?: "?"
                val groups = getGroupsByContact(id)

                _persons.value =
                    persons.value + listOf(Person(displayName, groups, hasPhoneNumber, id))
            }
            cursor.close()
        }
    }

    private fun getGroupsByContact(contactId: Long): List<Long> {
        val uri: Uri = ContactsContract.Data.CONTENT_URI
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.GroupMembership.CONTACT_ID,
            ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID, // DATA1

        )
        val where = ContactsContract.Data.MIMETYPE + " = ? AND " +
                ContactsContract.CommonDataKinds.GroupMembership.CONTACT_ID + " = ?"

        val selectionArgs = arrayOf(
            ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE, contactId.toString()
        )
        val sortOrder = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY

        val cursor = context.contentResolver.query(
            uri, projection, where, selectionArgs, sortOrder
        )

        val resultList = mutableListOf<Long>()
        cursor?.let {
            val groupIdRef =
                it.getColumnIndex(ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID)
            while (it.moveToNext()) {
                val groupId = it.getLong(groupIdRef)
                resultList.add(groupId)
            }
            it.close()
        }
        return resultList
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
//        Log.e("my", DatabaseUtils.dumpCursorToString(cursor))
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
            _groups.value = groupsList
        }
    }

    private fun getEventsCursor(): Cursor? {
        val uri: Uri = ContactsContract.Data.CONTENT_URI
        /*.buildUpon()
        .appendQueryParameter(
            ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE,
            ContactsContract.Data.MIMETYPE
        )
        .build()*/
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Event._ID,
            ContactsContract.CommonDataKinds.Event.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Event.CONTACT_ID,
            ContactsContract.CommonDataKinds.Event.LABEL,
            ContactsContract.CommonDataKinds.Event.START_DATE,
            ContactsContract.CommonDataKinds.Event.TYPE,
        )

        val where = ContactsContract.Data.MIMETYPE + "= ?"
        val selectionArgs = arrayOf(
            ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE
        )
        val sortOrder = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
        return context.contentResolver.query(uri, projection, where, selectionArgs, sortOrder)
    }

    private fun eventsUpdate() = CoroutineScope(Dispatchers.Default).launch {
        val cursor = getEventsCursor()
        cursor?.let {
            val idIdx = it.getColumnIndex(ContactsContract.CommonDataKinds.Event._ID)
            val labelIdx = it.getColumnIndex(ContactsContract.CommonDataKinds.Event.LABEL)
            val contactIdIdx =
                it.getColumnIndex(ContactsContract.CommonDataKinds.Event.CONTACT_ID)
            val dateIdx = it.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE)
            val typeIdx = it.getColumnIndex(ContactsContract.CommonDataKinds.Event.TYPE)

            while (it.moveToNext()) {
                val id = it.getLong(idIdx)
                val contactId = it.getLong(contactIdIdx)
                val label = it.getString(labelIdx) ?: ""
                val date = it.getString(dateIdx)
                val type = it.getInt(typeIdx)
                _events.value = events.value +
                        listOf(Event(label, date, type, contactId, id))
            }
            it.close()
        }
    }

    /*   private fun getAggregationContacts(id: Int) {
           val uri = ContactsContract.Contacts.CONTENT_URI.buildUpon()
               .appendEncodedPath(id.toString())
               .appendPath(ContactsContract.Contacts.AggregationSuggestions.CONTENT_DIRECTORY)
               .appendQueryParameter("limit", "3")
               .build()
           val cursor = context.contentResolver.query(
               uri,
               arrayOf(
                   ContactsContract.Contacts.DISPLAY_NAME,
                   ContactsContract.Contacts._ID,
                   ContactsContract.Contacts.LOOKUP_KEY
               ),
               null, null, null
           );
           Log.d("my", DatabaseUtils.dumpCursorToString(cursor))
       }*/

    fun getPersonInfo(contactId: Long): PersonInfo {
        val uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, contactId.toString())
        val cursor = context.contentResolver.query(
            uri, null, null, null, null
        )
        Log.e("my", DatabaseUtils.dumpCursorToString(cursor))

/*        val groupUri = Uri.withAppendedPath(uri, ContactsContract.CommonDataKinds.GroupMembership.CONTACT_ID)
        val cursorInfo = contentResolver.query(groupUri, null, null, null, null)
        Log.e("my", DatabaseUtils.dumpCursorToString(cursorInfo))*/

        // All info:
        val infoUri = Uri.withAppendedPath(uri, ContactsContract.Contacts.Entity.CONTENT_DIRECTORY)
        val cursorInfo = context.contentResolver.query(
            infoUri, null, null, null, null
        )

        Log.e("my", DatabaseUtils.dumpCursorToString(cursorInfo))

        val phones = mutableListOf<Phone>()
//        val emails = mutableListOf<Phone>()

        cursorInfo?.let {
            val mimeTypeIdx = it.getColumnIndex(ContactsContract.Data.MIMETYPE)
            val data1Idx = it.getColumnIndex(ContactsContract.Data.DATA1)
            val data2Idx = it.getColumnIndex(ContactsContract.Data.DATA2)

            while (it.moveToNext()) {
                val mimeType = it.getString(mimeTypeIdx) ?: ""
                val data1 = it.getString(data1Idx) ?: "?"
                val data2 = it.getString(data2Idx) ?: "0"

                when (mimeType) {
                    ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE -> {
                        phones.add(Phone(data1, data2.toIntDefault(0)))
                    }
/*                    ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE -> {
                        emails.add(Phone(data1, data2.toIntDefault(0)))
                    }*/
                }
            }
            it.close()
        }
        return PersonInfo(phones)
    }
}

fun String.toIntDefault(default: Int) =
    try {
        this.toInt()
    } catch (e: Exception) {
        default
    }