package ru.nifontbus.persons_data.repository

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.ContactsContract
import android.provider.ContactsContract.Contacts.openContactPhotoInputStream
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.nifontbus.persons_domain.model.Person
import ru.nifontbus.persons_domain.model.PersonInfo
import ru.nifontbus.persons_domain.model.Phone
import ru.nifontbus.persons_domain.repository.PersonsRepository
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class PersonsRepositoryImpl(
    private val context: Context
) : PersonsRepository {

    private val _persons = MutableStateFlow<List<Person>>(emptyList())
    override val persons = _persons.asStateFlow()

    init {
        personsUpdate()
    }

    private fun personsUpdate() = CoroutineScope(Dispatchers.Default).launch {
        val uri = ContactsContract.Contacts.CONTENT_URI

        val projection = arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.LOOKUP_KEY,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.Contacts.HAS_PHONE_NUMBER,
            ContactsContract.Contacts.PHOTO_URI,
        )

        val sortOrder = ContactsContract.Contacts.DISPLAY_NAME

        val cursor = context.contentResolver.query(
            uri, projection, null, null, sortOrder
        )

        cursor?.let {
            val idRef = it.getColumnIndex(ContactsContract.Contacts._ID)
            val lookupRef = it.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY)
            val displayNameRef = it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
            val hasPhoneNumberRef = it.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)

            while (it.moveToNext()) {
                val lookup = it.getString(lookupRef)
                val id = it.getLong(idRef)
                val hasPhoneNumber = it.getInt(hasPhoneNumberRef) == 1
                val displayName = it.getString(displayNameRef) ?: "?"
                val groups = getGroupsByContact(id)

                _persons.value =
                    persons.value + listOf(
                        Person(
                            displayName = displayName,
                            groups = groups,
                            hasPhoneNumber = hasPhoneNumber,
                            photo = getPhotoById(id),
                            id = id,
                            lookup = lookup
                        )
                    )
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

    override suspend fun getPersonInfo(contactId: Long): PersonInfo {
        val uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, contactId.toString())

        val infoUri = Uri.withAppendedPath(uri, ContactsContract.Contacts.Entity.CONTENT_DIRECTORY)
        val cursorInfo = context.contentResolver.query(
            infoUri, null, null, null, null
        )
//        Log.e("my", DatabaseUtils.dumpCursorToString(cursorInfo))
        val phones = mutableListOf<Phone>()

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
                }
            }
            it.close()
        }
        return suspendCoroutine {
            it.resume(PersonInfo(phones))
        }
    }

    private suspend fun getPhotoById(contactId: Long): ImageBitmap? {
        val contactUri =
            ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId)
        val photoUri =
            Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY)
        val cursor: Cursor = context.contentResolver.query(
            photoUri,
            arrayOf(ContactsContract.Contacts.Photo.PHOTO),
            null,
            null,
            null
        )
            ?: return null

        return suspendCoroutine {
            it.resume(
                try {
                    if (cursor.moveToFirst()) {
                        val data = cursor.getBlob(0)
                        if (data != null) {
                            BitmapFactory.decodeByteArray(data, 0, data.size).asImageBitmap()
                        } else null
                    } else null

                } catch (e: Exception) {
                    null
                } finally {
                    cursor.close()
                }
            )
        }
    }

    override suspend fun getDisplayPhoto(contactId: Long): ImageBitmap? {
        val contactUri =
            ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId)
//        dispatchSyncHighResPhotoIntent(contactUri)
        return suspendCoroutine {
            it.resume(
                try {
                    val stream = openContactPhotoInputStream(
                        context.contentResolver,
                        contactUri, true
                    )
                    if (stream != null) BitmapFactory.decodeStream(stream).asImageBitmap()
                    else null
                } catch (e: Exception) {
                    null
                }
            )
        }
    }
} // EOC

fun String.toIntDefault(default: Int) =
    try {
        this.toInt()
    } catch (e: Exception) {
        default
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