package ru.nifontbus.contactsevents.domain.repository

import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.nifontbus.core.domain.model.PersonsGroup


class ContactsRepository(private val context: Context) {

    private val _groups = MutableStateFlow<List<PersonsGroup>>(emptyList())
    val groups = _groups.asStateFlow()

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
            _groups.value = groupsList
        }
    }

    //    https://stackoverflow.com/questions/57727876/android-contacts-high-res-displayphoto-not-showing-up
/*    private fun dispatchSyncHighResPhotoIntent(uri: Uri) {
//        val uri = ContentUris.withAppendedId(ContactsContract.RawContacts.CONTENT_URI, rawContactId)
        val intent = Intent()
        intent.setDataAndType(uri, ContactsContract.Contacts.CONTENT_ITEM_TYPE)
        intent.component = ComponentName(
            "com.google.android.syncadapters.contacts",
            "com.google.android.syncadapters.contacts.SyncHighResPhotoIntentService"
        )
        intent.action = "com.google.android.syncadapters.contacts.SYNC_HIGH_RES_PHOTO"
        context.startService(intent)
    }*/

//    https://www.grokkingandroid.com/use-contentobserver-to-listen-to-changes/
/*    inner class MyObserver(handler: Handler?) : ContentObserver(handler) {
        override fun onChange(selfChange: Boolean, uri: Uri?) {
            super.onChange(selfChange, uri)
            Log.e("my", "---> Change $uri")
        }
    }*/

} // EOC

fun String.toIntDefault(default: Int) =
    try {
        this.toInt()
    } catch (e: Exception) {
        default
    }

// +event:
// https://question-it.com/questions/5800521/dobavit-sobytie-dlja-kontakta-v-tablitsu-kontaktov-android

// Операции с данными:
// https://developer.android.com/reference/android/provider/ContactsContract.Data.html?authuser=1

// +contacts
// https://stackru.com/questions/32761829/vstavka-novogo-kontakta-programmno-cherez-moe-prilozhenie-bez-ispolzovaniya-intent

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

/*    private fun getEventsCursorById(id: Long): Cursor? {
        val uri: Uri = ContactsContract.Data.CONTENT_URI

        val projection = arrayOf(
            ContactsContract.Data.DATA_SET,
            ContactsContract.Data._ID,
            ContactsContract.Data.MIMETYPE,
        )

        val where = ContactsContract.Data._ID + "=?" + "=?" +
                " AND " + ContactsContract.Data.MIMETYPE
        val selectionArgs = arrayOf(
            id.toString(),
            ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE
        )
        return context.contentResolver.query(uri, projection, where, selectionArgs, null)
    }*/