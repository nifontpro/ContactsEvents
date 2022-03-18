package ru.nifontbus.worker_data.repository

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.nifontbus.events_domain.model.Event
import ru.nifontbus.events_domain.model.EventType
import ru.nifontbus.worker_data.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

class WorkerContacts @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun getEventsNow(): List<Event> {
        val list = mutableListOf<Event>()
        val cursor = getEventsCursor()
        cursor?.let {
            val labelIdx = it.getColumnIndex(ContactsContract.CommonDataKinds.Event.LABEL)
            val nameIdx = it.getColumnIndex(ContactsContract.CommonDataKinds.Event.DISPLAY_NAME)
            val dateIdx = it.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE)
            val typeIdx = it.getColumnIndex(ContactsContract.CommonDataKinds.Event.TYPE)

            val now = LocalDate.now().asString()
            val day40minus = LocalDate.now().minusDays(39).asString()

            while (it.moveToNext()) {
                val date = it.getString(dateIdx)
                var label = it.getString(labelIdx) ?: ""
                val name = it.getString(nameIdx) ?: ""
                var type = it.getInt(typeIdx)
                val isNewLifeDay =
                    type == EventType.CUSTOM && label == context.getString(R.string.sDayOfRepose)
                val is40day = date == day40minus && isNewLifeDay
                if (date == now || is40day) {

                    if (is40day) {
                        type = EventType.CUSTOM
                        label = "40 days of $date"
                    } else {
                        if (isNewLifeDay) {
                            type = EventType.NEW_LIFE_DAY
                            label = ""
                        }
                    }

                    list.add(
                        Event(
                            label = label,
                            date = date,
                            type = type,
                            displayName = name
                        )
                    )
                }
            }
            it.close()
        }
        return list
    }

    private fun getEventsCursor(): Cursor? {
        val uri: Uri = ContactsContract.Data.CONTENT_URI
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Event.DISPLAY_NAME,
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
}

fun LocalDate.asString(
    pattern: String = "yyyy-MM-dd",
    locale: Locale = Locale.getDefault()
): String {
    val formatter = DateTimeFormatter.ofPattern(pattern, locale)
    return formatter.format(this)
}