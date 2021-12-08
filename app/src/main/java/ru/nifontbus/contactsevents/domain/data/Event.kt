package ru.nifontbus.contactsevents.domain.data

import android.content.Context
import android.provider.ContactsContract
import ru.nifontbus.contactsevents.R

data class Event(
    val label: String = "",
    val date: String = "",
    val type: Int = -1,
    val personId: Long = -1,
    val id: Long = -1
) {

    fun getDescription(context: Context) =
        when (type) {
            EventType.CUSTOM -> label
            EventType.NEW_LIFE_DAY -> context.getString(R.string.sNewLifeDay)
            else -> context.getString(ContactsContract.CommonDataKinds.Event.getTypeResource(type))
        }
}