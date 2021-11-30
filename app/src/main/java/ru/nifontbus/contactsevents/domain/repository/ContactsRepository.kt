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
import ru.nifontbus.contactsevents.domain.data.ContactsGroup
import ru.nifontbus.contactsevents.domain.data.Person


class ContactsRepository(private val context: Context) {

    private val _persons = MutableStateFlow<List<Person>>(emptyList())
    val persons = _persons.asStateFlow()

    private val _groups = MutableStateFlow<List<ContactsGroup>>(emptyList())
    val groups = _groups.asStateFlow()

    init {
        personsUpdate()
        groupsUpdate()
        eventsUpdate()
    }

    // +contacts
    // https://stackru.com/questions/32761829/vstavka-novogo-kontakta-programmno-cherez-moe-prilozhenie-bez-ispolzovaniya-intent

    private fun personsUpdate() = CoroutineScope(Dispatchers.Default).launch {
        val uri = ContactsContract.Contacts.CONTENT_URI
        /*.buildUpon()
            .appendQueryParameter(ContactsContract.AggregationExceptions.TYPE,
                ContactsContract.AggregationExceptions.TYPE_KEEP_SEPARATE.toString())
            .build()
*/
        val projection = arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.Contacts.HAS_PHONE_NUMBER,
        )
        val where = ContactsContract.Contacts.DISPLAY_NAME + " LIKE ?"
        val selectionArgs = arrayOf("А%")
        val sortOrder = ContactsContract.Contacts.DISPLAY_NAME

        val cursor = context.contentResolver.query(
            uri, projection, null, null, sortOrder
        )

//        Log.e("my", DatabaseUtils.dumpCursorToString(cursor))

        val personsList = mutableListOf<Person>()
        cursor?.let {
//            val keyRef = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY)
            val idRef = it.getColumnIndex(ContactsContract.Contacts._ID)
            val displayNameRef = it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
            val hasPhoneNumberRef = it.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)

            while (it.moveToNext()) {
//                val key = it.getString(keyRef) // LookUp Key
                val id = it.getInt(idRef)
                val hasPhoneNumber = it.getInt(hasPhoneNumberRef) == 1
                val displayName = it.getString(displayNameRef) ?: "?"
                val groups = getGroupsByContact(id)

                personsList.add(Person(displayName, groups, hasPhoneNumber, id))
                _persons.value = personsList.toList()
            }
            cursor.close()
        }
    }

    private fun getGroupsByContact(contactId: Int): List<Int> {
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

        val resultList = mutableListOf<Int>()
        cursor?.let {
            val groupIdRef =
                it.getColumnIndex(ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID)
            while (it.moveToNext()) {
                val groupId = it.getInt(groupIdRef)
                resultList.add(groupId)
            }
            cursor.close()
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
        Log.e("my", DatabaseUtils.dumpCursorToString(cursor))
        cursor?.let {

            val idIdx = it.getColumnIndex(ContactsContract.Groups._ID)
            val titleIdx = it.getColumnIndex(ContactsContract.Groups.TITLE)
            val accountIdx = it.getColumnIndex(ContactsContract.Groups.ACCOUNT_NAME)

            val groupsList = mutableListOf<ContactsGroup>()

            while (it.moveToNext()) {
                val id = it.getInt(idIdx)
                val title = it.getString(titleIdx)
                val account = it.getString(accountIdx)
                val newGroup = ContactsGroup(title, account, id)
                groupsList.add(newGroup)
            }
            cursor.close()
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
        Log.e("my", DatabaseUtils.dumpCursorToString(cursor))

        cursor?.let {

            val labelIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.LABEL)
            val contactIdIdx =
                cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.CONTACT_ID)
            val dateIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE)
            val typeIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.TYPE)

            while (cursor.moveToNext()) {
                val date = cursor.getString(dateIdx)
                val contactId = cursor.getInt(contactIdIdx)
                val label = cursor.getString(labelIdx)
                val type = cursor.getInt(typeIdx)
                val typeStr =
                    context.getString(ContactsContract.CommonDataKinds.Event.getTypeResource(type))

                Log.d("my", "$label, ContactId: $contactId, type = $typeStr")
            }
            cursor.close()
        }
    }

    private fun getAggregationContacts(id: Int) {
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
    }
}