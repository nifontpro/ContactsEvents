package ru.nifontbus.contactsevents.domain.data

import android.content.Context
import android.provider.ContactsContract
import ru.nifontbus.contactsevents.R

data class Template(
    val id: Long = -1,
    val type: Int = EventType.CUSTOM,
    val label: String = "",
) {

    fun getDescription(context: Context) =
        when (type) {
            EventType.CUSTOM -> {
                if (label.isEmpty())
                    context.getString(ContactsContract.CommonDataKinds.Event.getTypeResource(type))
                else label
            }
            EventType.NEW_LIFE_DAY -> context.getString(R.string.sNewLifeDay)
            else -> context.getString(ContactsContract.CommonDataKinds.Event.getTypeResource(type))
        }

    fun getDescriptionForSelect(context: Context) =
        when (type) {
            EventType.CUSTOM -> {
                if (label.isEmpty()) ""
                else label
            }
            EventType.NEW_LIFE_DAY -> context.getString(R.string.sNewLifeDay)
            else -> context.getString(ContactsContract.CommonDataKinds.Event.getTypeResource(type))
        }
}