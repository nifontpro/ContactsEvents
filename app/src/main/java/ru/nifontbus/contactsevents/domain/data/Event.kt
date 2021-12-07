package ru.nifontbus.contactsevents.domain.data

import android.content.Context
import android.provider.ContactsContract

data class Event(
    val label: String = "",
    val date: String = "",
    val type: Int = -1,
    val personId: Long = -1,
    val id: Long = -1
) {

    fun getDescription(context: Context) =
        if (type == ContactsContract.CommonDataKinds.Event.TYPE_CUSTOM) {
            label
        } else {
            context.getString(ContactsContract.CommonDataKinds.Event.getTypeResource(type))
        }
}