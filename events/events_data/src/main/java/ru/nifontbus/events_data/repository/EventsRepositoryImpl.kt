package ru.nifontbus.events_data.repository

import android.content.ContentProviderOperation
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.nifontbus.core.domain.model.Resource
import ru.nifontbus.events_data.R
import ru.nifontbus.events_domain.model.Event
import ru.nifontbus.events_domain.model.EventType
import ru.nifontbus.events_domain.repository.EventsRepository
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class EventsRepositoryImpl(
    private val context: Context
) : EventsRepository {

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    override val events: StateFlow<List<Event>> = _events.asStateFlow()

    init {
        eventsUpdate()
    }

    private fun getEventsCursor(): Cursor? {
        val uri: Uri = ContactsContract.Data.CONTENT_URI
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Event._ID,
            ContactsContract.CommonDataKinds.Event.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Event.CONTACT_ID,
            ContactsContract.CommonDataKinds.Event.LOOKUP_KEY,
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
            val lookupIdx =
                it.getColumnIndex(ContactsContract.CommonDataKinds.Event.LOOKUP_KEY)
            val dateIdx = it.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE)
            val typeIdx = it.getColumnIndex(ContactsContract.CommonDataKinds.Event.TYPE)

            _events.value = emptyList()

            while (it.moveToNext()) {
                val id = it.getLong(idIdx)
                val lookup = it.getString(lookupIdx)
                val contactId = it.getLong(contactIdIdx)
                var label = it.getString(labelIdx) ?: ""
                val date = it.getString(dateIdx)
                var type = it.getInt(typeIdx)

                if (type == EventType.CUSTOM && label == context.getString(R.string.sDayOfRepose)) {
                    type = EventType.NEW_LIFE_DAY
                    label = ""
                }

                _events.value = events.value +
                        listOf(
                            Event(
                                label = label,
                                date = date,
                                type = type,
                                personId = contactId,
                                id = id,
                                lookup = lookup
                            )
                        )
            }
            it.close()
        }
    }

    override suspend fun addEvent(event: Event): Resource<Unit> {
        return suspendCoroutine {
            val exc = context.getString(R.string.sFailedCreateEvent)
            it.resume(
                try {
                    val values = getContentValuesForEvent(event)
                    val idStr =
                        context.contentResolver.insert(
                            ContactsContract.Data.CONTENT_URI, values
                        )?.lastPathSegment ?: throw Exception(exc)

                    val id = idStr.toLong()
                    val newEvent = event.copy(id = id)
                    _events.value = events.value + newEvent

                    Resource.Success(context.getString(R.string.sEventCreateSuccessful))
                } catch (e: Exception) {
                    Resource.Error(e.localizedMessage ?: exc)
                }
            )
        }
    }

    override suspend fun updateEvent(newEvent: Event, oldEvent: Event): Resource<Unit> {
        val whereBuf = StringBuffer()

        whereBuf.append(ContactsContract.Data._ID)
        whereBuf.append("=")
        whereBuf.append(newEvent.id)

        whereBuf.append(" and ")
        whereBuf.append(ContactsContract.Data.CONTACT_ID)
        whereBuf.append("=")
        whereBuf.append(newEvent.personId)

        whereBuf.append(" and ")
        whereBuf.append(ContactsContract.Data.MIMETYPE)
        whereBuf.append(" = '")
        val mimetype = ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE
        whereBuf.append(mimetype)
        whereBuf.append("'")

        return suspendCoroutine {
            val exc = context.getString(R.string.sFailedUpdateEvent)
            it.resume(
                try {
                    val values = getContentValuesForEvent(newEvent, false)
                    context.contentResolver.update(
                        ContactsContract.Data.CONTENT_URI, values, whereBuf.toString(), null
                    )

                    _events.value = events.value - oldEvent + newEvent

                    Resource.Success(context.getString(R.string.sEventUpdateSuccessful))
                } catch (e: Exception) {
                    Resource.Error(e.localizedMessage ?: exc)
                }
            )
        }
    }

    private fun getContentValuesForEvent(newEvent: Event, isNew: Boolean = true): ContentValues {
        val values = ContentValues()
        val rawId = getRawId(newEvent.personId)

        if (isNew) {
            values.put(
                ContactsContract.Data.MIMETYPE,
                ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE
            )
            values.put(ContactsContract.CommonDataKinds.Event.RAW_CONTACT_ID, rawId)
        }

        when (newEvent.type) {
            EventType.CUSTOM -> {
                values.put(ContactsContract.CommonDataKinds.Event.LABEL, newEvent.label)
                values.put(ContactsContract.CommonDataKinds.Event.TYPE, EventType.CUSTOM)
            }
            EventType.NEW_LIFE_DAY -> {
                values.put(
                    ContactsContract.CommonDataKinds.Event.LABEL,
                    context.getString(R.string.sDayOfRepose)
                )
                values.put(ContactsContract.CommonDataKinds.Event.TYPE, EventType.CUSTOM)
            }
            else -> {
                if (!isNew) {
                    values.put(ContactsContract.CommonDataKinds.Event.LABEL, "")
                }
                values.put(ContactsContract.CommonDataKinds.Event.TYPE, newEvent.type)
            }
        }

        values.put(ContactsContract.CommonDataKinds.Event.START_DATE, newEvent.date)
        return values
    }

    override suspend fun deleteEvent(event: Event): Resource<Unit> {

        val ops = ArrayList<ContentProviderOperation>()

        ops.add(
            ContentProviderOperation.newDelete(ContactsContract.Data.CONTENT_URI)
                .withSelection(
                    ContactsContract.Data._ID + "=? AND " +
                            ContactsContract.Data.MIMETYPE + "=?", // Необязательно, но для надежности
                    arrayOf(
                        event.id.toString(),
                        ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE
                    )
                )
                .build()
        )

        return suspendCoroutine {

            it.resume(
                try {
                    context.contentResolver.applyBatch(ContactsContract.AUTHORITY, ops)
                    _events.value = events.value - event
                    Resource.Success(context.getString(R.string.sEventDeleteSuccessful))
                } catch (e: Exception) {
                    Resource.Error(
                        e.localizedMessage ?: context.getString(R.string.sErrorDeleteEvent)
                    )
                }
            )
        }
    }

    /**
     * Получение Raw_Id по Id контакта
     */
    private fun getRawId(contactId: Long): Long {
        var rawId = -1L
        val uri = ContactsContract.RawContacts.CONTENT_URI
        val projection = arrayOf(ContactsContract.RawContacts._ID)
        val selection = ContactsContract.RawContacts.CONTACT_ID + " = ?"
        val selectionArgs = arrayOf(contactId.toString())
        val cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, null)
        cursor?.let {
            val rawIdx = it.getColumnIndex(ContactsContract.RawContacts._ID)
            it.moveToFirst()
            rawId = it.getLong(rawIdx)
            cursor.close()
        }
        return rawId
    }
}
